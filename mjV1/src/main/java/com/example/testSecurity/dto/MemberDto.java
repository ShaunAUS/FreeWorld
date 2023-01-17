package com.example.testSecurity.dto;

import com.example.testSecurity.Enum.RoleType;
import com.example.testSecurity.entity.Member;
import com.example.testSecurity.utils.MapperUtils;
import io.swagger.annotations.ApiModel;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class MemberDto {

    @Getter
    @Builder
    @Setter
    @ApiModel(value = "MemberDto.Info", description = "멤버 정보")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {

        private String userName;
        private LocalDateTime createDate;
        private LocalDateTime updateDate;
        private RoleType roleType;

        public static MemberDto.Info toDto(Member member) {
            return MapperUtils.getMapper()
                .typeMap(Member.class, MemberDto.Info.class)
                .addMappings(mapper -> {
                    mapper.using(RoleType.INTEGER_ROLE_TYPE_CONVERTER)
                        .map(Member::getRoleType, MemberDto.Info::setRoleType);
                })
                .map(member)
                .setCurrentTime();
        }

        private Info setCurrentTime() {
            this.createDate = LocalDateTime.now(ZoneOffset.UTC);
            this.updateDate = LocalDateTime.now(ZoneOffset.UTC);
            return this;
        }
    }


    @Getter
    @ApiModel(value = "MemberDto.Create", description = "멤버 생성")
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {

        private String userName;   // = LoginForm UserName
        private String password;
        @ApiModelProperty(value = "권한 - ( ANONYMOUS:로그인전 회원, GENERAL_MEMBER:일반회원, ADMIN:어드민 )")
        private RoleType roleType;

        public void insertEncodedPassword(String encodedPassword) {
            this.password = encodedPassword;
        }

        public static Member toEntity(MemberDto.Create createDto) {
            return MapperUtils.getMapper()
                .typeMap(MemberDto.Create.class, Member.class)
                .addMappings(mapper -> mapper.using(RoleType.ROLE_TYPE_INTEGER_CONVERTER)
                    .map(Create::getRoleType, Member::setRoleType))
                .map(createDto);
        }

    }


}
