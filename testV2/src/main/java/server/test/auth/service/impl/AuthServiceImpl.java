package server.test.auth.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.test.auth.dto.AuthToken;
import server.test.auth.dto.LoginForm;
import server.test.auth.dto.MemberAccess;
import server.test.auth.service.AuthService;
import server.test.common.JwtProvider;
import server.test.config.AppProperties;
import server.test.dto.MemberDto;
import server.test.entity.Member;
import server.test.exception.ServiceProcessException;
import server.test.exception.enums.ServiceMessage;
import server.test.repository.MemberAccessRepository;
import server.test.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final MemberAccessRepository memberAccessRepository;
    private final AppProperties appProperties;
    private final JwtProvider jwtProvider;

    ObjectMapper obj = new ObjectMapper();

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
            .manager(obj.convertValue(member.get(), MemberDto.Info.class))
            .build();

    }

    @Override
    public void logout(String token) {

        //expire token time
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
