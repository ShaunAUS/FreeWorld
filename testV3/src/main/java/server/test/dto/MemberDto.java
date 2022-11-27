package server.test.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@RequiredArgsConstructor
public class MemberDto {

    @Getter
    @Builder
    @ApiModel(value = "MemberDto.Info", description = "기본정보")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private String name;
        private Integer age;
        private String createDate;
        private String updateDate;
    }


    @Getter
    @Builder
    @ApiModel(value = "MemberDto.Create", description = "기본정보")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private String userName;   // = LoginForm UserName
        private String password;
        //private String role;
        private LocalDateTime createDate = LocalDateTime.now(ZoneOffset.UTC);
        private LocalDateTime updateDate = LocalDateTime.now(ZoneOffset.UTC);

        public void insertEncodedPassword(String encodedPassword) {
            this.password = encodedPassword;
        }
    }


}
