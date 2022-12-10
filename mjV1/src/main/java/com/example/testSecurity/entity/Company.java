package com.example.testSecurity.entity;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

}
