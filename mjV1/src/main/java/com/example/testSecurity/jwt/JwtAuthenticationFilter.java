package com.example.testSecurity.jwt;

import com.example.testSecurity.config.AppProperties;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        final String token = getToken(request);

        //JWT 토큰이 없으면 
        if (token != null) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext()
                .setAuthentication(authentication);   // Authentication 객체를 SecurityContextHolder에 저장
        }
        filterChain.doFilter(request, response);
    }

    //헤더에서 jwt 토큰꺼내기
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(AppProperties.AUTH_TOKEN_NAME);// Header = Authorization key값 , token 은  value

        if (StringUtils.hasText(token)) ;
        return token;
    }

}

