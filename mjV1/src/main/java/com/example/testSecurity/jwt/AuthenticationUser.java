package com.example.testSecurity.jwt;

import com.example.testSecurity.exception.enums.ServiceMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationUser {

    private String issuer;
    private String audience;
    private String sessionId;

    private Integer memberNo;

    //로그인 멤버 가져오기
    public static Integer extractMemberNo(Authentication authentication) {
        var user = from(authentication);
        if (null == user.getMemberNo() || user.getMemberNo() == 0) {
            throw new AuthorizationServiceException(ServiceMessage.NOT_AUTHORIZED.getMessage());
        }
        return user.getMemberNo();
    }

    public static AuthenticationUser from(Authentication authentication) {
        return JwtProvider.getCredential(authentication).user;
    }
}
