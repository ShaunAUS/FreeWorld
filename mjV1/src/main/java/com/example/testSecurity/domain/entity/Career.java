package com.example.testSecurity.domain.entity;

import static com.example.testSecurity.domain.enums.CategoryType.CATEGORY_TYPE_INTEGER_CONVERTER;
import static com.example.testSecurity.domain.enums.CategoryType.INTEGER_CATEGORY_TYPE_CONVERTER;

import com.example.testSecurity.domain.dto.career.CareerCreateDto;
import com.example.testSecurity.domain.dto.career.CareerInfoDto;
import com.example.testSecurity.domain.dto.career.CareerUpdateDto;
import com.example.testSecurity.domain.utils.MapperUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_no")
    private Long no;
    @ApiModelProperty(value = "회사이름")
    @Column(nullable = false)
    private String companyName;
    @ApiModelProperty(value = "담당업무")
    @Column(nullable = false)
    private String assignedTask;
    @ApiModelProperty(value = "담당업무 설명")
    @Column(nullable = false)
    private String description;
    @ApiModelProperty(value = "경력 년월 - `yyyymm` ")
    @Column(nullable = false)
    private String startPeriod;
    @ApiModelProperty(value = "경력 년월 - `yyyymm` ")
    @Column(nullable = false)
    private String finishPeriod;

    @ApiModelProperty(value = "카테고리")
    @Column(nullable = false)
    private Integer category;
    @ApiModelProperty(value = "카테고리 상세")
    @Column(nullable = false)
    private Integer categoryDetail;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_no")
    private Profile profile;


    public static Career of(CareerCreateDto careerCreateDto) {
        return MapperUtils.getMapper()
            .typeMap(CareerCreateDto.class, Career.class)
            .addMappings(mapper -> {
                mapper.using(CATEGORY_TYPE_INTEGER_CONVERTER)
                    .map(CareerCreateDto::getCategory, Career::setCategory);
            })
            .map(careerCreateDto);
    }

    public void changeProfile(Profile savedProfile) {
        this.profile = savedProfile;
    }

    public static CareerInfoDto toCareerInfoDTO(Career career) {
        return MapperUtils.getMapper()
            .typeMap(Career.class, CareerInfoDto.class)
            .map(career);
    }


    public void updateCareer(CareerUpdateDto careerUpdateDto) {
        MapperUtils.getMapper()
            .typeMap(CareerUpdateDto.class, Career.class)
            .addMappings(mapper -> {
                mapper.using(CATEGORY_TYPE_INTEGER_CONVERTER)
                    .map(CareerUpdateDto::getCategory, Career::setCategory);
            })
            .map(careerUpdateDto, this);

    }
}
