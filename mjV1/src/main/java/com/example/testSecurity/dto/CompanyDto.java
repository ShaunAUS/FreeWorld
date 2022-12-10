package com.example.testSecurity.dto;

import com.example.testSecurity.Enum.CategoryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@RequiredArgsConstructor
public class CompanyDto {


    @Getter
    @Builder
    @Setter
    @ApiModel(value = "CompanyDto.Create", description = "회사 생성")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @ApiModelProperty(value = "회사이름")
        private String name;
        @ApiModelProperty(value = "연락번호")
        private String contactNumber;
        @ApiModelProperty(value = "회사주소")
        private String address;
        @ApiModelProperty(value = "사업자 번호")
        private Integer businessNumber;
        @ApiModelProperty(value = "인증여부")
        private Boolean isCertificated = Boolean.FALSE;
    }

    @Getter
    @Builder
    @Setter
    @ApiModel(value = "CompanyDto.Info", description = "회사 정보")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        @ApiModelProperty(value = "회사이름")
        private String name;
        @ApiModelProperty(value = "연락번호")
        private String contactNumber;
        @ApiModelProperty(value = "회사주소")
        private String address;
        @ApiModelProperty(value = "사업자 번호")
        private Integer businessNumber;
        @ApiModelProperty(value = "인증여부")
        private Boolean isCertificated;
    }
}
