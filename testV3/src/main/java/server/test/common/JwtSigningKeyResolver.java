package server.test.common;

import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import server.test.config.AppProperties;

@RequiredArgsConstructor
public class JwtSigningKeyResolver extends SigningKeyResolverAdapter {

    private final AppProperties properties;

    /**
     * 관리자 권한 jwt 인증 키
     */
    @Getter
    private Key memberAuthKey;

    @PostConstruct
    public void init() {
        this.memberAuthKey = Keys.hmacShaKeyFor(properties.getMemberAuthKey());
    }



}

