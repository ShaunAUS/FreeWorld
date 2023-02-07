package com.example.testSecurity.auth.service;

import com.example.testSecurity.auth.dto.AuthToken;
import com.example.testSecurity.auth.dto.LoginForm;
import com.example.testSecurity.dto.member.MemberCreateDto;
import com.example.testSecurity.dto.member.MemberInfoDto;


public interface AuthService {

    MemberInfoDto createMember(MemberCreateDto createDto);

    AuthToken login(LoginForm loginForm);

    void logout(String token);

    AuthToken refresh(String token, String refreshToken);
}
