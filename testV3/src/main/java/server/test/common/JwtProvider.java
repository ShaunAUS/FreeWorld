package server.test.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import server.test.config.AppProperties;
import server.test.entity.Member;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtSigningKeyResolver signingKeyResolver;
    private final AppProperties properties;


    public String getJwtToken(Member member,Long memberAccessId) {

        return Jwts.builder()
            //header
            .setHeaderParam("typ", "JWT")
            .setHeaderParam("alg", "HS256")

            //payload
            //.setIssuer("test")
            .setSubject(AppProperties.LOGIN_SERVICE)
            .setAudience(member.getUserName())
            .claim(AppProperties.ACCESS_ID,memberAccessId)  // accessId
            .claim("name", member.getUserName())
            .claim("email", member.getEmail())
            .claim("roles", member.getRole())
            .setExpiration(new Date(System.currentTimeMillis()+60*properties.getAccessHoldTimeMillis()))  // 10분

            //SecretKey or privateKey 로 잠궈 시그니처 만듬
            //publicKey 는 사용권장 x
            .signWith(signingKeyResolver.getMemberAuthKey())
            .compact();
    }

    //토큰파싱
    public Jws<Claims> parsingToken(String token,long allowedClockSkewSeconds){
        return Jwts.parserBuilder()
            .setSigningKeyResolver(signingKeyResolver) // SecretKey or privateKey 아닐경우
            .setAllowedClockSkewSeconds(allowedClockSkewSeconds)
            .build()
            .parseClaimsJws(token);
    }


    //JWT 토큰 파싱 -> claim 추출 (MemberAccessId)
    public long getAccessId(String token){
        final Jws<Claims> claimsJws = parsingToken(token, (120 - properties.getAccessHoldTime()) * 60); //토큰 파싱
        return claimsJws.getBody().get(properties.SESSION_ID, Long.class);

    }


}
