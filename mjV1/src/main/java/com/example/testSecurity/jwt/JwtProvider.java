package com.example.testSecurity.jwt;

import com.example.testSecurity.Enum.RoleType;
import com.example.testSecurity.config.AppProperties;
import com.example.testSecurity.entity.Member;
import com.example.testSecurity.exception.enums.ServiceMessage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtSigningKeyResolver signingKeyResolver;
    private final AppProperties properties;


    public static Credential getCredential(Authentication authentication) {
        if (Optional.ofNullable(authentication).isEmpty()) {
            throw new AuthenticationServiceException(ServiceMessage.NOT_AUTHORIZED.getMessage());
        }
        // cast authentication.getCredentials() as Credential
        Credential credentials = (Credential) authentication.getCredentials();
        if (Optional.ofNullable(credentials).isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException(
                ServiceMessage.NOT_AUTHORIZED.getMessage());
        }
        return credentials;
    }


    public String getJwtToken(Member member, Long memberAccessId) {

        return Jwts.builder()
            //header
            .setHeaderParam("typ", "JWT")
            .setHeaderParam("alg", "HS256")

            //payload
            .setSubject(AppProperties.LOGIN_SERVICE)
            .setAudience(member.getUserName())
            .claim(AppProperties.ACCESS_ID, memberAccessId)  // accessId
            .claim("name", member.getUserName())
            .claim("password", member.getPassword())
            //.claim("roles", Arrays.asList(RoleType.valueOf(member.getRoleType())))
            .claim("roles", Arrays.asList(RoleType.valueOf(member.getRoleType())))
            .setExpiration(new Date(
                System.currentTimeMillis() + 60 * properties.getAccessHoldTimeMillis()))  // 15분

            //SecretKey or privateKey 로 잠궈 시그니처 만듬
            //publicKey 는 사용권장 x
            .signWith(signingKeyResolver.getMemberAuthKey())
            .compact();
    }

    //토큰파싱
    public Jws<Claims> parsingToken(@NonNull String token, long allowedClockSkewSeconds) {
        return Jwts.parserBuilder()
            .setSigningKeyResolver(signingKeyResolver) // SecretKey or privateKey 아닐경우
            .setAllowedClockSkewSeconds(allowedClockSkewSeconds)
            .build()
            .parseClaimsJws(token);
    }


    //JWT 토큰 파싱 -> claim 추출 (MemberAccessId)
    public long getAccessId(String token) {
        final Jws<Claims> claimsJws = parsingToken(token,
            (120 - properties.getAccessHoldTime()) * 60); //토큰 파싱
        return claimsJws.getBody()
            .get(properties.SESSION_ID, Long.class); // claim으로 넣어뒀던 값을 해당 타입으로 반환
    }


    //토큰값이 없으면 수행 로직
    //DB에서 계정정보를 가져온뒤에 UserDetails 타입으로 만든뒤 UsernamePasswordAuthenticationToken 생성(계정,비번,권한)
    // => Authentication가져오기 -> SecurityContextHolder에 넣기 ->세션에 저장 =>인증성공
    public Authentication getAuthentication(String token) {

        Jws<Claims> claimsJws = null;

        try {
            claimsJws = parsingToken(token, properties.getAccessHoldTime() * 60l);
        } catch (ExpiredJwtException e) {
            //인증 유효 시간이 지난 경우
            Claims claims = e.getClaims();
            UserDetails expiredUser = User.withUsername(claims.getAudience())
                .accountExpired(true)
                .disabled(true)
                .password("")
                .roles()
                .build();

            //계정,권한 등등
            return new UsernamePasswordAuthenticationToken(expiredUser, null,
                Collections.emptyList());

        } catch (JwtException e) {

            //인증키 검증 오류, 포맷 오류등등
            UserDetails invalidUser = User.withUsername("invalidUser")
                .disabled(true)
                .password("")
                .roles()
                .build();
            //계정,권한 등등
            return new UsernamePasswordAuthenticationToken(invalidUser, null,
                Collections.emptyList());
        }

        //JWT 문제 없으면
        //15분 재인증할 때마다 계정 유효성과 role를 다시 읽어온다
        final Long sessionId = claimsJws.getBody()
            .get(AppProperties.ACCESS_ID, Long.class);   //claim 에 저장한 정보 해당 타입으로 반환
        String audience = claimsJws.getBody().getAudience();

        switch (claimsJws.getBody().getSubject()) {

            //subject
            case AppProperties.LOGIN_SERVICE: {
                ArrayList<String> roles = claimsJws.getBody()
                    .get("roles", ArrayList.class);  // jwt - 권한claim 추출

                UserDetails manager = User.withUsername(audience + ":" + sessionId)
                    //TODO roles.toArray(new String[roles.size()]) - entity 롤추가하면 넣기
                    .roles(roles.toArray(new String[roles.size()]))
                    .password("")
                    .build();

                //계정,권한 등등
                return new UsernamePasswordAuthenticationToken(manager, null,
                    manager.getAuthorities());
            }

            default:
                throw new IllegalStateException();
        }
    }

    @Builder
    public static class Credential {

        public String token;
        public AuthenticationUser user;
        public Jws<Claims> claims;
    }


}
