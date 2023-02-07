package com.example.testSecurity.service.jwt;

import com.example.testSecurity.server.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtSigningKeyResolver extends SigningKeyResolverAdapter {

    private final AppProperties properties;

    /**
     * 멤버 권한 jwt 인증 키
     */
    @Getter
    private Key memberAuthKey;

    //
    @PostConstruct
    public void init() {
        // secretKey = 이 키로 jwt 잠금= properties 에 적어둔 키
        this.memberAuthKey = Keys.hmacShaKeyFor("i|\\,|`{S^#C'}L4-7$i\">o)}b^-~\\test".getBytes());
    }


    //Returns the signing key that should be used to validate a digital signature for the Claims JWS with the specified header and claims.
    // = JWT 잠근키 (secretKey) 반환
    @Override
    public Key resolveSigningKey(JwsHeader header, Claims claims) {

        //JWT sub claim
        switch (claims.getSubject()) {
            case AppProperties.LOGIN_SERVICE: {
                return memberAuthKey;
            }
            default:
                return null;
        }
    }


}

