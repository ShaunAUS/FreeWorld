package com.example.testSecurity.config;

import com.example.testSecurity.jwt.JwtAuthenticationFilter;
import com.example.testSecurity.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws
        Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .cors().and()  // 교차출처 리소스 공유 x

            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()// 세션 사용 x
            // http request
            .authorizeRequests()
            .antMatchers("/**").permitAll()
            .antMatchers("/v1/**").permitAll()
            .anyRequest().authenticated()

            .and()

            //id/password 인증 필터 전에 JWT 인증
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
