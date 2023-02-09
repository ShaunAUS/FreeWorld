package com.example.testSecurity.domain.dto.member;

import com.example.testSecurity.domain.enums.RoleType;
import com.example.testSecurity.domain.entity.Member;
import com.example.testSecurity.domain.utils.MapperUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@ApiModel(description = "멤버 생성 DTO")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberCreateDto {

    private String userName;   // = LoginForm UserName
    private String password;
    @ApiModelProperty(value = "권한 - ( ANONYMOUS:로그인전 회원, GENERAL_MEMBER:일반회원, ADMIN:어드민 )")
    private RoleType roleType;

    public void insertEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }


}
