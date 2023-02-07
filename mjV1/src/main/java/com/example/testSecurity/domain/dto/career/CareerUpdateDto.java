package com.example.testSecurity.domain.dto.career;

import com.example.testSecurity.domain.enums.CategoryDetailType;
import com.example.testSecurity.domain.enums.CategoryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@Setter
@ApiModel(description = "경력 수정 DTO")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CareerUpdateDto {


    @ApiModelProperty(value = "회사이름")
    private String companyName;
    @ApiModelProperty(value = "담당업무")
    private String assignedTask;
    @ApiModelProperty(value = "담당업무 설명")
    private String description;
    @ApiModelProperty(value = "경력 년월 - `yyyymm` ")
    private Integer startPeriod;
    @ApiModelProperty(value = "경력 년월 - `yyyymm` ")
    private Integer finishPeriod;
    @ApiModelProperty(value = "카테고리")
    private CategoryType category;
    @ApiModelProperty(value = "카테고리 상세")
    private CategoryDetailType categoryDetail;


}
