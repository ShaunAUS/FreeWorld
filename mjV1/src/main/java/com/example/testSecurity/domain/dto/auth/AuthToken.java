package com.example.testSecurity.domain.dto.auth;

import com.example.testSecurity.domain.dto.member.MemberInfoDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@Builder
@AllArgsConstructor
@ToString
@ApiModel(value = "AuthToken", description = "jwt 토큰")
public class AuthToken {

    /**
     * jwt 토큰
     */
    @ApiModelProperty("jwt 토큰")
    private String jwt;

    /**
     * 접속 유저 정보
     */
    @ApiModelProperty("접속 유저 정보")
    private MemberInfoDto managerInfo;

    /**
     * 리플레쉬 토큰
     */
    @ApiModelProperty("리플레쉬 토큰")
    private String refreshToken;

}
