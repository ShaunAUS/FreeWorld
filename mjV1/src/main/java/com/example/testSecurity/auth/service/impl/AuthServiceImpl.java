package com.example.testSecurity.auth.service.impl;

import com.example.testSecurity.auth.dto.AuthToken;
import com.example.testSecurity.auth.dto.LoginForm;
import com.example.testSecurity.auth.entity.MemberAccess;
import com.example.testSecurity.auth.service.AuthService;
import com.example.testSecurity.jwt.JwtProvider;
import com.example.testSecurity.config.AppProperties;
import com.example.testSecurity.dto.MemberDto;
import com.example.testSecurity.entity.Member;
import com.example.testSecurity.exception.ServiceProcessException;
import com.example.testSecurity.exception.enums.ServiceMessage;
import com.example.testSecurity.repository.MemberAccessRepository;
import com.example.testSecurity.repository.MemberJpaRepository;
import com.example.testSecurity.utils.MapperUtils;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MemberJpaRepository memberJpaRepository;

    private final PasswordEncoder passwordEncoder;

    private final MemberAccessRepository memberAccessRepository;
    private final AppProperties appProperties;
    private final JwtProvider jwtProvider;


    @Override
    public MemberDto.Info createMember(MemberDto.Create createDto) {

        log.info("멤버 추가 시작");
        //아이디 중복체크
        checkDuplicateUserName(createDto);

        createDto.insertEncodedPassword(passwordEncoder.encode(createDto.getPassword()));
        Member dMember = memberJpaRepository.save(MemberDto.Create.toEntity(createDto));


        //ModelMapper는 해당 클래스의 기본 생성자를 이용해 객체를 생성하고 setter를 이용해 매핑을 한다.
        return MemberDto.Info.toDto(dMember);
    }


    @Override
    public AuthToken login(LoginForm loginForm) {

        Optional<Member> member = checkUserNameAndPassword(loginForm);

        LocalDateTime loginTime = LocalDateTime.now(ZoneOffset.UTC);

        //접근기록
        MemberAccess memberAccess = MemberAccess.builder()
            .member(member.get())
            .refreshToken(UUID.randomUUID().toString())
            .tokenCreateToken(loginTime)
            .tokenExpireDate(loginTime.plusMinutes(appProperties.getAccessHoldTime()))
            .build();
        memberAccessRepository.save(memberAccess);

        //토큰생성
        String token = jwtProvider.getJwtToken(member.get(), memberAccess.getId());

        return AuthToken.builder()
            .jwt(token)
            .refreshToken(memberAccess.getRefreshToken())
            .manager(MapperUtils.getMapper().map(member.get(), MemberDto.Info.class))
            .build();

    }

    @Override
    public void logout(String token) {

        //Access 정보로 토큰시간 만료시키기
        MemberAccess memberAccess = memberAccessRepository.findById(jwtProvider.getAccessId(token)).get();

        if (memberAccess == null) {
            throw new ServiceProcessException(ServiceMessage.NOT_FOUND_ACCESS_INFO);
        }
        memberAccess.expireJwtToken();
    }

    @Override
    public void refresh() {

    }


    //로그인
    private Optional<Member> checkUserNameAndPassword(LoginForm loginForm) {

        //일단 이름으로 DB에 있나 확인
        Optional<Member> member = memberJpaRepository.findByUserName(loginForm.getUserName());

        //null이면
        member.orElseThrow(() -> new ServiceProcessException(ServiceMessage.USER_NOT_FOUND));

        //비밀번호 확인
        if (!passwordEncoder.matches(loginForm.getPassword(), member.get().getPassword())) {
            throw new ServiceProcessException(ServiceMessage.WRONG_PASSWORD);
        }

        return member;
    }

    //만들때
    private void checkDuplicateUserName(MemberDto.Create createDto) {

        Optional<Member> optionalMember = memberJpaRepository.findByUserName(createDto.getUserName());
        if (optionalMember.isPresent()) {
            if (optionalMember.get().getUserName().equals(createDto.getUserName())) {
                throw new ServiceProcessException(ServiceMessage.DUPLICATE_USERNAME);
            }
        }
    }

}
