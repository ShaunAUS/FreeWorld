package com.example.testSecurity.dto.profile;

import com.example.testSecurity.dto.career.CareerUpdateDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@Setter
@ApiModel(description = "프로파일 수정 DTO")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProfileUpdateDto {

    @ApiModelProperty(value = "이름")
    private String name;
    @ApiModelProperty(value = "소개")
    private String introduce;

    @ApiModelProperty(value = "년차")
    private Integer experienceYear;

    @ApiModelProperty(value = "이메일")
    private String email;
    @ApiModelProperty(value = "연락처")
    private String contactNumber;
    private List<CareerUpdateDto> updateCareers;

}
