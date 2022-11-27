package server.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws
        Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .cors().and()  // 교차출처 리소스 공유 x

            // http request
            .authorizeRequests()
            .antMatchers("/**/refresh-token").permitAll()
            .antMatchers("/**/change-mode").permitAll()
            .antMatchers("/**/login").permitAll()
            .antMatchers("/v1/**").permitAll()
            .antMatchers("/v2/api-docs", "/v3/api-docs", "/swagger-resources/**", "/webjars/**",
                "/swagger-ui.html", "/swagger-ui/*").permitAll()

            .anyRequest().authenticated()

/*
            .formLogin()
            .disable()
            .csrf()
            .disable()
            .headers()
            .disable()
            .httpBasic()
            .disable()
            .rememberMe()
            .disable()
            .logout()
            .disable()*/

            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) ;  // 세션 사용 x

 /*           .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler())
            .authenticationEntryPoint(authenticationEntryPoint())*/
           // .and()
            //.addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}




