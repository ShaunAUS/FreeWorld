package com.example.testSecurity.domain.dto.profile;

import com.example.testSecurity.domain.enums.CategoryDetailType;
import com.example.testSecurity.domain.enums.CategoryType;
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
@ApiModel(description = "프로파일 검색조건 DTO")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileSearchConditionDto {

    @ApiModelProperty(value = "카테고리")
    private CategoryType categoryType;
    @ApiModelProperty(value = "카테고리 디테일")
    private CategoryDetailType categoryDetailType;
    @ApiModelProperty(value = "년차")
    private Integer experienceYear;

}
