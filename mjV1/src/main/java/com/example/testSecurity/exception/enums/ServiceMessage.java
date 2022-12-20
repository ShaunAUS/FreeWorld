package com.example.testSecurity.exception.enums;

import java.util.stream.Stream;

/**
 * 인가인증 에러를 표현하는 열거형
 */
public enum ServiceMessage {

    USER_NOT_FOUND(-1, "사용자를 찾을 수 없습니다"),
    WRONG_PASSWORD(-2,"비밀번호가 일치하지 않습니다." ),

    DUPLICATE_USERNAME(-2,"아이디 중복" ),
    NOT_FOUND_ACCESS_INFO(-3,"접근기록을 찾을 수 없습니다" ),

    NOT_AUTHORIZED(-4,"인증되지 않은 사용자" ),
    NOT_FOUND(-5,"해당하는 정보가 없습니다." ),
    COMPANY_NOT_FOUND(-6, "해당 회사가 존재하지 않습니다" ),
    NOT_FOUND_PROFILE(-7,"프로파일이 존재하지 않습니다" );


    



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
