package com.example.testSecurity.domain.dto.member;

import com.example.testSecurity.domain.enums.RoleType;
import com.example.testSecurity.domain.entity.Member;
import com.example.testSecurity.domain.utils.MapperUtils;
import io.swagger.annotations.ApiModel;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@ApiModel(description = "멤버 Info DTO")
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDto extends MemberCreateDto {

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public static MemberInfoDto toDto(Member member) {
        return MapperUtils.getMapper()
            .typeMap(Member.class, MemberInfoDto.class)
            .addMappings(mapper -> {
                mapper.using(RoleType.INTEGER_ROLE_TYPE_CONVERTER)
                    .map(Member::getRoleType, MemberInfoDto::setRoleType);
            })
            .map(member)
            .setCurrentTime();
    }

    private MemberInfoDto setCurrentTime() {
        this.createDate = LocalDateTime.now(ZoneOffset.UTC);
        this.updateDate = LocalDateTime.now(ZoneOffset.UTC);
        return this;
    }

}
