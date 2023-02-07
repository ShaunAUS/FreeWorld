package com.example.testSecurity.domain.dto.profile;

import static com.example.testSecurity.domain.entity.Profile.CAREER_LIST_TO_INFO_LIST;

import com.example.testSecurity.domain.dto.career.CareerCreateDto;
import com.example.testSecurity.domain.dto.career.CareerInfoDto;
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
import org.modelmapper.Converter;

@ToString
@Getter
@Setter
@ApiModel(description = "프로파일 Info DTO")
@NoArgsConstructor
@AllArgsConstructor
public class ProfileInfoDto extends ProfileUpdateDto {

    private List<CareerInfoDto> infoCareers;


    public static final Converter<List<CareerCreateDto>, List<CareerInfoDto>> CAREER_CREATE_LIST_TO_INFO_LIST =
        context -> context.getSource() == null ? null : context.getSource().stream()
            .map(CareerInfoDto::toDto) // career create -> career Info
            .collect(Collectors.toList());


    public static ProfileInfoDto toInfoDto(Profile profile) {
        return MapperUtils.getMapper()
            .typeMap(Profile.class, ProfileInfoDto.class)
            .addMappings(mapper -> {
                mapper.using(CAREER_LIST_TO_INFO_LIST)
                    .map(Profile::getCareers, ProfileInfoDto::setInfoCareers);
            })
            .map(profile);
    }


}
