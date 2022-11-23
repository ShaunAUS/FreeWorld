package server.test.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.test.auth.dto.AuthToken;
import server.test.auth.dto.LoginForm;
import server.test.auth.service.AuthService;
import server.test.config.AppProperties;
import springfox.documentation.annotations.ApiIgnore;

@Api("로그인")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1")
public class AuthController {

    final private AuthService authService;

    @ApiOperation(value = "Login", notes = "Login")
    @PostMapping("/login")
    public AuthToken login(@RequestBody LoginForm loginForm){
        return authService.login(loginForm);
    }

    @ApiOperation(value = "Logout", notes = "Logout")
    @PostMapping("/logout")
    public void logout(
        @ApiIgnore @RequestHeader(AppProperties.AUTH_TOKEN_NAME) String token   //Header = key값 ,  value 는 token
    ){
        authService.logout(token);
    }


}
