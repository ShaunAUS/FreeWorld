package server.test.auth.service;

import server.test.auth.dto.AuthToken;
import server.test.auth.dto.LoginForm;
import server.test.dto.MemberDto;
import server.test.entity.Member;

public interface AuthService {

    MemberDto.Info createMember(MemberDto.Create createDto);

    AuthToken login(LoginForm loginForm);

    void logout(String token);

    void refresh();
}
