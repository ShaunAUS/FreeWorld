package server.test.auth.service;

import server.test.auth.dto.AuthToken;
import server.test.auth.dto.LoginForm;
import server.test.entity.Member;

public interface AuthService {

    AuthToken login(LoginForm loginForm);

    void logout(String token);

    void refresh();
}
