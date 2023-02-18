package com.example.testSecurity.domain.entity;

import com.example.testSecurity.domain.dto.member.MemberCreateDto;
import com.example.testSecurity.domain.enums.RoleType;
import com.example.testSecurity.domain.utils.MapperUtils;
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
    @Column(nullable = false)
    private String userName;   // = LoginForm UserName
    @ApiModelProperty(value = "접속 비밀번호")
    @Column(nullable = false)
    private String password;
    @ApiModelProperty(value = "권한")
    @Column(nullable = false)
    private Integer roleType;

    public static Member of(MemberCreateDto createDto) {
        return MapperUtils.getMapper()
            .typeMap(MemberCreateDto.class, Member.class)
            .addMappings(
                mapper -> mapper.using(RoleType.ROLE_TYPE_INTEGER_CONVERTER)
                    .map(MemberCreateDto::getRoleType, Member::setRoleType))
            .map(createDto);
    }
}
