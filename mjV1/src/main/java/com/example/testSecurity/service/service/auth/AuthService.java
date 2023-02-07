package com.example.testSecurity.service.service.auth;

import com.example.testSecurity.domain.dto.auth.AuthToken;
import com.example.testSecurity.domain.dto.auth.LoginForm;
import com.example.testSecurity.domain.dto.member.MemberCreateDto;
import com.example.testSecurity.domain.dto.member.MemberInfoDto;


public interface AuthService {

    MemberInfoDto createMember(MemberCreateDto createDto);

    AuthToken login(LoginForm loginForm);

    void logout(String token);

    AuthToken refresh(String token, String refreshToken);
}
