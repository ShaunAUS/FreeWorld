package com.example.testSecurity.entity;

import com.example.testSecurity.dto.ArticleDto;
import com.example.testSecurity.dto.CompanyDto;
import com.example.testSecurity.utils.MapperUtils;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_no")
    private Long no;

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

    @OneToOne
    @JoinColumn(name = "member_no")
    private Member member;

    public static void changeCompany(CompanyDto.Create companyCreateDTO, Company company) {
        MapperUtils.getMapper()
                .typeMap(CompanyDto.Create.class, Company.class)
                .map(companyCreateDTO, company);
    }
}
