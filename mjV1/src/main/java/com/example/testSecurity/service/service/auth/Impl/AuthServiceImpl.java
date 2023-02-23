package com.example.testSecurity.service.service.auth.Impl;


import com.example.testSecurity.domain.dto.auth.AuthToken;
import com.example.testSecurity.domain.dto.auth.LoginForm;
import com.example.testSecurity.domain.dto.member.MemberCreateDto;
import com.example.testSecurity.domain.dto.member.MemberInfoDto;
import com.example.testSecurity.domain.repository.MemberAccessRepository;
import com.example.testSecurity.domain.repository.MemberJpaRepository;
import com.example.testSecurity.server.config.AppProperties;
import com.example.testSecurity.domain.entity.Member;
import com.example.testSecurity.domain.entity.auth.MemberAccess;
import com.example.testSecurity.service.exception.ServiceProcessException;
import com.example.testSecurity.service.exception.enums.ServiceMessage;
import com.example.testSecurity.service.jwt.JwtProvider;
import com.example.testSecurity.service.service.auth.AuthService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public MemberInfoDto createMember(MemberCreateDto createDto) {

        //아이디 중복체크
        checkDuplicateUserName(createDto);

        createDto.insertEncodedPassword(passwordEncoder.encode(createDto.getPassword()));
        Member savedMember = memberJpaRepository.save(Member.of(createDto));

        //ModelMapper는 해당 클래스의 기본 생성자를 이용해 객체를 생성하고 setter를 이용해 매핑을 한다.
        return MemberInfoDto.of(savedMember);
    }


    @Override
    public AuthToken login(LoginForm loginForm) {

        Optional<Member> member = checkUserNameAndPassword(loginForm);

        LocalDateTime loginTime = LocalDateTime.now(ZoneOffset.UTC);

        //접근기록
        MemberAccess memberAccess = MemberAccess.builder()
            .member(member.get())
            .refreshToken(UUID.randomUUID().toString())
            .tokenCreateDate(loginTime)
            .tokenExpireDate(loginTime.plusMinutes(appProperties.getAccessHoldTime()))
            .build();
        memberAccessRepository.save(memberAccess);

        return AuthToken.builder()
            .jwt(jwtProvider.getJwtToken(member.get(), memberAccess))
            .refreshToken(memberAccess.getRefreshToken())
            .managerInfo(MemberInfoDto.of(member.get()))
            .build();

    }

    @Override
    @Transactional
    public void logout(String token) {

        //Access 정보로 토큰시간 만료시키기
        Optional<MemberAccess> memberAccessById = memberAccessRepository.findById(
            jwtProvider.getAccessId(token));

        if (!memberAccessById.isPresent()) {
            throw new ServiceProcessException(ServiceMessage.NOT_FOUND_ACCESS_INFO);
        }

        MemberAccess memberAccess = memberAccessById.get();

        //Access 기록만 수정
        memberAccess.expireJwtToken();
        Long accessId = memberAccess.getId();

        // 로그아웃시 사용한 토큰 저장
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        vop.set(String.valueOf(accessId), token);

        // 15분뒤 기록 삭제
        redisTemplate.expire(String.valueOf(accessId), 900, TimeUnit.SECONDS);


    }

    @Override
    @Transactional
    public AuthToken refresh(String token, String refreshToken) {

        Long accessId = jwtProvider.getAccessId(token);

        //refresh token 으로 로그인 멤버 가져오기
        Optional<MemberAccess> memberAccessIdAndRefreshToken = memberAccessRepository.findByIdAndRefreshToken(
            accessId, refreshToken);
        if (!memberAccessIdAndRefreshToken.isPresent()) {
            throw new UsernameNotFoundException("Authentication error");
        }
        MemberAccess memberAccess = memberAccessIdAndRefreshToken.get();

        //로그인 시간을 늘릴 멤버
        final Member loginManager = memberAccess.getMember();

        //MemberAccess 시간 늘리기
        memberAccess.refresh(appProperties.getAccessHoldTime());

        // 로그인 멤버로 토큰 재발급
        Member member = memberJpaRepository.findById(loginManager.getNo()).get();
        final String refreshedToken = jwtProvider.getJwtToken(member, memberAccess);
        
        return AuthToken.builder()
            .jwt(refreshedToken)
            .managerInfo(MemberInfoDto.of(member))
            .build();
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
    private void checkDuplicateUserName(MemberCreateDto createDto) {

        Optional<Member> optionalMember = memberJpaRepository.findByUserName(
            createDto.getUserName());
        if (optionalMember.isPresent()) {
            if (optionalMember.get().getUserName().equals(createDto.getUserName())) {
                throw new ServiceProcessException(ServiceMessage.DUPLICATE_USERNAME);
            }
        }
    }

}
