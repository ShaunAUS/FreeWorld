package com.example.testSecurity.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime{

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

    @OneToOne
    @JoinColumn(name = "profile_no")
    private Profile profile;
}
