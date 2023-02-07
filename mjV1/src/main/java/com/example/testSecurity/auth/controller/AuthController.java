package com.example.testSecurity.auth.controller;

import com.example.testSecurity.auth.dto.AuthToken;
import com.example.testSecurity.auth.dto.LoginForm;
import com.example.testSecurity.auth.service.AuthService;
import com.example.testSecurity.config.AppProperties;
import com.example.testSecurity.dto.member.MemberCreateDto;
import com.example.testSecurity.dto.member.MemberInfoDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api("로그인")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1")
public class AuthController {

    final private AuthService authService;


    @ApiOperation(value = "createMember", notes = "멤버추가")
    @PostMapping("/create")
    public MemberInfoDto createMember(@RequestBody MemberCreateDto createDto) {
        return authService.createMember(createDto);
    }


    @ApiOperation(value = "Login", notes = "JWT 토큰반환")
    @PostMapping("/sign-in")
    public AuthToken login(@RequestBody LoginForm loginForm) {
        return authService.login(loginForm);
    }

    @ApiOperation(value = "Logout", notes = "로그아웃")
    @PostMapping("/sign-out")
    public void logout(
        @ApiIgnore @RequestHeader(AppProperties.AUTH_TOKEN_NAME) String token
        //Header = key값 ,  value 는 token
    ) {
        authService.logout(token);
    }


    @ApiOperation("리프레쉬 토큰")
    @PostMapping("/refresh-token")
    public AuthToken refresh(
        @ApiIgnore @RequestHeader(AppProperties.AUTH_TOKEN_NAME) String token,
        @ApiParam(value = "refreshToken", required = true) @RequestParam String refreshToken
    ) {
        return authService.refresh(token, refreshToken);
    }


}
