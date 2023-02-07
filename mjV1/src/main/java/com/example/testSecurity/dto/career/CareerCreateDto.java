package com.example.testSecurity.dto.career;

import static com.example.testSecurity.Enum.CategoryType.CATEGORY_TYPE_INTEGER_CONVERTER;

import com.example.testSecurity.Enum.CategoryDetailType;
import com.example.testSecurity.Enum.CategoryType;
import com.example.testSecurity.entity.Career;
import com.example.testSecurity.utils.MapperUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    public Career toEntity() {
        return MapperUtils.getMapper()
            .typeMap(CareerCreateDto.class, Career.class)
            .addMappings(mapper -> {
                mapper.using(CATEGORY_TYPE_INTEGER_CONVERTER)
                    .map(CareerCreateDto::getCategory, Career::setCategory);
            })
            .map(this);
    }

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
