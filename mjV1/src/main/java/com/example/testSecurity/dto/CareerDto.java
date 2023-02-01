package com.example.testSecurity.dto;

import static com.example.testSecurity.Enum.CategoryType.CATEGORY_TYPE_INTEGER_CONVERTER;

import com.example.testSecurity.Enum.CategoryDetailType;
import com.example.testSecurity.Enum.CategoryType;
import com.example.testSecurity.entity.Article;
import com.example.testSecurity.entity.Career;
import com.example.testSecurity.entity.Profile;
import com.example.testSecurity.utils.MapperUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.modelmapper.Converter;

@RequiredArgsConstructor
public class CareerDto {

    @Getter
    @Builder
    @Setter
    @ApiModel(value = "CareerDto.Create", description = "경력 생성")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

        @ApiModelProperty(value = "회사이름")
        private String companyName;
        @ApiModelProperty(value = "담당업무")
        private String assignedTask;
        @ApiModelProperty(value = "설명")
        private String description;
        @ApiModelProperty(value = "경력 년월 - `yyyymm` ")
        private Integer startPeriod;
        @ApiModelProperty(value = "경력 년월 - `yyyymm` ")
        private Integer finishPeriod;
        @ApiModelProperty(value = "카테고리")
        private CategoryType category;
        @ApiModelProperty(value = "카테고리 상세")
        private CategoryDetailType categoryDetail;

        public Career toEntity() {
            return MapperUtils.getMapper()
                .typeMap(CareerDto.Create.class, Career.class)
                .addMappings(mapper -> {
                    mapper.using(CATEGORY_TYPE_INTEGER_CONVERTER)
                        .map(CareerDto.Create::getCategory, Career::setCategory);
                })
                .map(this);
        }

        public static void update(Create create, Career career) {
            MapperUtils.getMapper()
                .typeMap(CareerDto.Create.class, Career.class)
                .addMappings(mapper -> {
                    mapper.using(CATEGORY_TYPE_INTEGER_CONVERTER)
                        .map(CareerDto.Create::getCategory, Career::setCategory);
                })
                .map(create, career);
        }

    }

    @Getter
    @Builder
    @Setter
    @ApiModel(value = "CareerDto.Info", description = "경력 정보")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {

        @ApiModelProperty(value = "회사이름")
        private String companyName;
        @ApiModelProperty(value = "담당업무")
        private String assignedTask;
        @ApiModelProperty(value = "담당업무 설명")
        private String description;
        @ApiModelProperty(value = "경력 년월 - `yyyymm` ")
        private String startPeriod;
        @ApiModelProperty(value = "경력 년월 - `yyyymm` ")
        private String finishPeriod;
        @ApiModelProperty(value = "카테고리")
        private CategoryType category;

        public static CareerDto.Info toDto(Create create) {
            return MapperUtils.getMapper()
                .typeMap(CareerDto.Create.class, CareerDto.Info.class)
                .map(create);
        }
    }
}
