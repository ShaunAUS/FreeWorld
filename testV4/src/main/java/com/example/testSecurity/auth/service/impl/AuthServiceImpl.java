package com.example.testSecurity.auth.service.impl;

import com.example.testSecurity.auth.dto.AuthToken;
import com.example.testSecurity.auth.dto.LoginForm;
import com.example.testSecurity.auth.entity.MemberAccess;
import com.example.testSecurity.auth.service.AuthService;
import com.example.testSecurity.common.JwtProvider;
import com.example.testSecurity.config.AppProperties;
import com.example.testSecurity.dto.MemberDto;
import com.example.testSecurity.entity.Member;
import com.example.testSecurity.exception.ServiceProcessException;
import com.example.testSecurity.exception.enums.ServiceMessage;
import com.example.testSecurity.repository.MemberAccessRepository;
import com.example.testSecurity.repository.MemberRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final MemberAccessRepository memberAccessRepository;
    private final AppProperties appProperties;
    private final JwtProvider jwtProvider;


    ModelMapper modelMapper = new ModelMapper();


    @Override
    public MemberDto.Info createMember(MemberDto.Create createDto) {
        
        //TODO 중복체크

        log.info("멤버 추가 시작");
        createDto.insertEncodedPassword(passwordEncoder.encode(createDto.getPassword()));

        Member savedMember = memberRepository.save(modelMapper.map(createDto,Member.class));

        //ModelMapper는 해당 클래스의 기본 생성자를 이용해 객체를 생성하고 setter를 이용해 매핑을 한다.
         return modelMapper.map(savedMember,MemberDto.Info.class);
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
        String token = jwtProvider.getJwtToken(member.get(),memberAccess.getId());

        return AuthToken.builder()
            .jwt(token)
            .refreshToken(memberAccess.getRefreshToken())
            .manager(modelMapper.map(member.get(), MemberDto.Info.class))
            .build();

    }

    @Override
    public void logout(String token) {

        //Access 정보로 토큰시간 만료시키기
        MemberAccess memberAccess = memberAccessRepository.findById(jwtProvider.getAccessId(token)).get();

        if(memberAccess == null){
            throw new ServiceProcessException(ServiceMessage.NOT_FOUND_ACCESS_INFO);
        }
        memberAccess.expireJwtToken();
    }

    @Override
    public void refresh() {

    }


    private Optional<Member> checkUserNameAndPassword(LoginForm loginForm) {
        //일단 이름으로 DB에 있나 확인
        Optional<Member> member = memberRepository.findByUserName(loginForm.getUserName());
        member.orElseThrow(() -> new ServiceProcessException(ServiceMessage.USER_NOT_FOUND));

        //비밀번호 확인
        if (!passwordEncoder.matches(loginForm.getPassword(), member.get().getPassword())) {
            throw new ServiceProcessException(ServiceMessage.WRONG_PASSWORD);
        }

        return member;
    }
}
