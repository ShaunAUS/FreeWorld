package com.example.testSecurity.entity;

import static com.example.testSecurity.Enum.CategoryType.CATEGORY_TYPE_INTEGER_CONVERTER;
import static com.example.testSecurity.Enum.CategoryType.INTEGER_CATEGORY_TYPE_CONVERTER;

import com.example.testSecurity.dto.CareerDto;
import com.example.testSecurity.dto.CareerDto.Create;
import com.example.testSecurity.utils.MapperUtils;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
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

    public void changeProfile(Profile savedProfile) {
        this.profile = savedProfile;
    }

    public static CareerDto.Info toCareerInfoDTO(Career career) {
        return MapperUtils.getMapper()
            .typeMap(Career.class, CareerDto.Info.class)
            .map(career);
    }

    public CareerDto.Info toInfoDto() {

        return MapperUtils.getMapper()
            .typeMap(Career.class, CareerDto.Info.class)
            .addMappings(mapper -> {
                mapper.using(INTEGER_CATEGORY_TYPE_CONVERTER)
                    .map(Career::getCategory, CareerDto.Info::setCategory);
            })
            .map(this);


    }

    public void updateCareer(CareerDto.Update careerUpdateDto) {
        MapperUtils.getMapper()
            .typeMap(CareerDto.Update.class, Career.class)
            .addMappings(mapper -> {
                mapper.using(CATEGORY_TYPE_INTEGER_CONVERTER)
                    .map(CareerDto.Update::getCategory, Career::setCategory);
            })
            .map(careerUpdateDto, this);

    }
}
