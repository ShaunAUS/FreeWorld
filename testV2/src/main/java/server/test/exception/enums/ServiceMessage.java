package server.test.exception.enums;

import java.util.stream.Stream;

/**
 * 인가인증 에러를 표현하는 열거형
 */
public enum ServiceMessage {

    USER_NOT_FOUND(-1, "사용자를 찾을 수 없습니다"),
    WRONG_PASSWORD(-2,"비밀번호가 일치하지 않습니다." ),
    NOT_FOUND_ACCESS_INFO(-3,"접근기록을 찾을 수 없습니다" );



    private int code;
    private String message;

    ServiceMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ServiceMessage of(int code) {
        return Stream.of(ServiceMessage.values())
            .filter(message -> message.getCode() == code)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static ServiceMessage of(String message) {
        return Stream.of(ServiceMessage.values())
            .filter(error -> error.getMessage().equals(message))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
