package com.example.testSecurity.dto.member;

import com.example.testSecurity.Enum.RoleType;
import com.example.testSecurity.entity.Member;
import com.example.testSecurity.utils.MapperUtils;
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

    public static Member toEntity(MemberCreateDto createDto) {
        return MapperUtils.getMapper()
            .typeMap(MemberCreateDto.class, Member.class)
            .addMappings(mapper -> mapper.using(RoleType.ROLE_TYPE_INTEGER_CONVERTER)
                .map(MemberCreateDto::getRoleType, Member::setRoleType))
            .map(createDto);
    }

}
