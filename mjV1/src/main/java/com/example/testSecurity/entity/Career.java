package com.example.testSecurity.entity;

import static com.example.testSecurity.Enum.CategoryType.CATEGORY_TYPE_INTEGER_CONVERTER;
import static com.example.testSecurity.Enum.CategoryType.INTEGER_CATEGORY_TYPE_CONVERTER;

import com.example.testSecurity.dto.CareerDto;
import com.example.testSecurity.dto.CareerDto.Create;
import com.example.testSecurity.dto.ProfileDto;
import com.example.testSecurity.dto.ProfileDto.Info;
import com.example.testSecurity.utils.MapperUtils;
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
    private String companyName;
    @ApiModelProperty(value = "담당업무")
    private String assignedTask;
    @ApiModelProperty(value = "설명")
    private String description;
    @ApiModelProperty(value = "년차")
    private Integer year;
    @ApiModelProperty(value = "카테고리")
    private Integer category;
    @ApiModelProperty(value = "카테고리 상세")
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

    public void updateCareer(Create create) {
        MapperUtils.getMapper()
            .typeMap(CareerDto.Create.class, Career.class)
            .addMappings(mapper -> {
                mapper.using(CATEGORY_TYPE_INTEGER_CONVERTER)
                    .map(CareerDto.Create::getCategory, Career::setCategory);
            })
            .map(create, this);

    }
}
