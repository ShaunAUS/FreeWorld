package com.example.testSecurity.dto.career;

import com.example.testSecurity.Enum.CategoryType;
import com.example.testSecurity.utils.MapperUtils;
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
@ApiModel(description = "경력 Info DTO")
@NoArgsConstructor
@SuperBuilder
public class CareerInfoDto extends CareerUpdateDto {


    public static CareerInfoDto toDto(CareerCreateDto create) {
        return MapperUtils.getMapper()
            .typeMap(CareerCreateDto.class, CareerInfoDto.class)
            .map(create);
    }

}
