package com.example.testSecurity.service.aop;

import com.example.testSecurity.domain.entity.Member;
import com.example.testSecurity.service.exception.ServiceProcessException;
import com.example.testSecurity.service.exception.enums.ServiceMessage;
import com.example.testSecurity.service.service.MemberService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

//AOP 애노테이션
@Component
@RequiredArgsConstructor
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<Member> findByUserName = memberService.findByUserName(getUserName(authentication));
        if (findByUserName.isPresent()) {
            return findByUserName.get();
        } else {
            throw new ServiceProcessException(ServiceMessage.USER_NOT_FOUND);
        }

    }

    private String getUserName(Authentication authentication) {
        final int i = authentication.getName().lastIndexOf(":");
        final String username =
            i > -1 ? authentication.getName().substring(0, i) : authentication.getName();
        return username;
    }
}
