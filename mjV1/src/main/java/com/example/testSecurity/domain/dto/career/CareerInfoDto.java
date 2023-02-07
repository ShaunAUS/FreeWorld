package com.example.testSecurity.domain.dto.career;

import com.example.testSecurity.domain.utils.MapperUtils;
import io.swagger.annotations.ApiModel;
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
