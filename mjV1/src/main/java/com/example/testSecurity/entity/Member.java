package com.example.testSecurity.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long no;

    @ApiModelProperty(value = "접속 아이디")
    private String userName;   // = LoginForm UserName
    @ApiModelProperty(value = "접속 비밀번호")
    private String password;
    @ApiModelProperty(value = "권한")
    private Integer roleType;

}
