package com.example.testSecurity.auth.service;

import com.example.testSecurity.auth.dto.AuthToken;
import com.example.testSecurity.auth.dto.LoginForm;
import com.example.testSecurity.dto.MemberDto;


public interface AuthService {

    MemberDto.Info createMember(MemberDto.Create createDto);

    AuthToken login(LoginForm loginForm);

    void logout(String token);

    void refresh();
}
