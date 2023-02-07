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

    public Profile toEntity() {
        return MapperUtils.getMapper()
            .typeMap(ProfileCreateDto.class, Profile.class)
            .addMappings(mapper -> {
                mapper.using(
                        CAREER_LIST_CONVERTER)   //List<CareerDto.Create> -> List<Career>
                    .map(ProfileCreateDto::getCareers, Profile::setCareers);
            })
            .map(this);
    }


    public static final Converter<List<CareerCreateDto>, List<Career>> CAREER_LIST_CONVERTER =
        context -> context.getSource() == null ? null : context.getSource().stream()
            .map(CareerCreateDto::toEntity)
            .collect(Collectors.toList());

    public ProfileInfoDto toInfo() {

        return MapperUtils.getMapper()
            .typeMap(ProfileCreateDto.class, ProfileInfoDto.class)

            //List<CareerDto.Create> career -> List<CareerDto.Info> career
            .addMappings(mapper -> {
                mapper.using(ProfileInfoDto.CAREER_CREATE_LIST_TO_INFO_LIST)
                    .map(ProfileCreateDto::getUpdateCareers, ProfileInfoDto::setInfoCareers);
            })
            .map(this);
    }


}
