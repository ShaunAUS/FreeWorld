package com.example.testSecurity.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("로그인폼")
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {

    @ApiModelProperty("로그인 아이디")
    private String userName;

    @ApiModelProperty("로그인 암호")
    private String password;

}
