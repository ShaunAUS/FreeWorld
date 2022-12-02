package com.example.testSecurity.dto;

import io.swagger.annotations.ApiModel;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;

@RequiredArgsConstructor
public class MemberDto {

    @Getter
    @Builder
    @Setter
    @ApiModel(value = "MemberDto.Info", description = "기본정보")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private String userName;
        private LocalDateTime createDate;
        private LocalDateTime updateDate;
    }


    @Getter
    @Builder
    @ApiModel(value = "MemberDto.Create", description = "회원가입")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create  {
        private String userName;   // = LoginForm UserName
        private String password;
        public void insertEncodedPassword(String encodedPassword) {
            this.password = encodedPassword;
        }
    }


}
