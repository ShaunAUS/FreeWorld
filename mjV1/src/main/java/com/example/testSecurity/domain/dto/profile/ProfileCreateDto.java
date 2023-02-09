package com.example.testSecurity.domain.dto.profile;


import com.example.testSecurity.domain.dto.career.CareerCreateDto;
import com.example.testSecurity.domain.entity.Career;
import com.example.testSecurity.domain.entity.Profile;
import com.example.testSecurity.domain.utils.MapperUtils;
import io.swagger.annotations.ApiModel;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.modelmapper.Converter;

@ToString
@Getter
@Setter
@ApiModel(description = "프로파일 생성 DTO")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProfileCreateDto extends ProfileUpdateDto {

    private List<CareerCreateDto> careers;


    public static final Converter<List<CareerCreateDto>, List<Career>> CAREER_LIST_CONVERTER =
        context -> context.getSource() == null ? null : context.getSource().stream()
            .map(Career::of)
            .collect(Collectors.toList());

}
