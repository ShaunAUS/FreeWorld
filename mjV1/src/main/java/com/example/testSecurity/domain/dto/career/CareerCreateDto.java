package com.example.testSecurity.domain.dto.career;

import static com.example.testSecurity.domain.enums.CategoryType.CATEGORY_TYPE_INTEGER_CONVERTER;

import com.example.testSecurity.domain.entity.Career;
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
@ApiModel(description = "경력 생성 DTO")
@NoArgsConstructor
@SuperBuilder
public class CareerCreateDto extends CareerUpdateDto {


    public static void update(CareerCreateDto create, Career career) {
        MapperUtils.getMapper()
            .typeMap(CareerCreateDto.class, Career.class)
            .addMappings(mapper -> {
                mapper.using(CATEGORY_TYPE_INTEGER_CONVERTER)
                    .map(CareerCreateDto::getCategory, Career::setCategory);
            })
            .map(create, career);
    }

}
