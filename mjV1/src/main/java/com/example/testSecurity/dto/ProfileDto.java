package com.example.testSecurity.dto;

import static com.example.testSecurity.Enum.CategoryType.CATEGORY_TYPE_INTEGER_CONVERTER;
import static com.example.testSecurity.dto.ProfileDto.Info.CAREER_CREATE_LIST_TO_INFO_LIST;
import static com.example.testSecurity.entity.Profile.CAREER_LIST_TO_INFO_LIST;

import com.example.testSecurity.Enum.CategoryDetailType;
import com.example.testSecurity.Enum.CategoryType;
import com.example.testSecurity.entity.Career;
import com.example.testSecurity.entity.Profile;
import com.example.testSecurity.utils.MapperUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.stream.Collectors;
import lombok.*;
import org.modelmapper.Converter;

import java.util.List;

@RequiredArgsConstructor
public class ProfileDto {


    @Getter
    @Builder
    @Setter
    @ApiModel(value = "ProfileDto.Create", description = "프로파일 생성")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

        @ApiModelProperty(value = "이름")
        private String name;
        @ApiModelProperty(value = "소개")
        private String introduce;

        @ApiModelProperty(value = "이메일")
        private String email;
        @ApiModelProperty(value = "연락처")
        private String contactNumber;
        private List<CareerDto.Create> careers;


        public Profile toEntity() {
            return MapperUtils.getMapper()
                .typeMap(ProfileDto.Create.class, Profile.class)
                .addMappings(mapper -> {
                    mapper.using(
                            CAREER_LIST_CONVERTER)   //List<CareerDto.Create> -> List<Career>
                        .map(ProfileDto.Create::getCareers, Profile::setCareers);
                })
                .map(this);
        }


        public static final Converter<List<CareerDto.Create>, List<Career>> CAREER_LIST_CONVERTER =
            context -> context.getSource() == null ? null : context.getSource().stream()
                .map(CareerDto.Create::toEntity)
                .collect(Collectors.toList());


/*        public static void update(ProfileDto.Create profileCreateDTO, Profile profile) {
            MapperUtils.getMapper()
                .typeMap(Create.class, Profile.class)
                .addMappings(mapper -> {
                    mapper.using(CAREER_CREATE_LIST_TO_ENTITY_LIST)
                        .map(ProfileDto.Create::getCareers, Profile::setCareers);
                })
                .map(profileCreateDTO, profile);
        }

        public static final Converter<List<CareerDto.Create>, List<Career>> CAREER_CREATE_LIST_TO_ENTITY_LIST =
            context -> context.getSource() == null ? null : context.getSource().stream()
                .map(create -> create.toEntity())
                .collect(Collectors.toList());*/

        public ProfileDto.Info toInfo() {

            return MapperUtils.getMapper()
                .typeMap(ProfileDto.Create.class, ProfileDto.Info.class)

                //List<CareerDto.Create> career -> List<CareerDto.Info> career
                .addMappings(mapper -> {
                    mapper.using(CAREER_CREATE_LIST_TO_INFO_LIST)
                        .map(ProfileDto.Create::getCareers, ProfileDto.Info::setCareers);
                })
                .map(this);
        }

    }

    @Getter
    @Builder
    @Setter
    @ApiModel(value = "ProfileDto.Info", description = "프로파일 정보")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {

        @ApiModelProperty(value = "이름")
        private String name;
        @ApiModelProperty(value = "이메일")
        private String email;
        @ApiModelProperty(value = "연락처")
        private String contactNumber;
        private List<CareerDto.Info> careers;


        public static final Converter<List<CareerDto.Create>, List<CareerDto.Info>> CAREER_CREATE_LIST_TO_INFO_LIST =
            context -> context.getSource() == null ? null : context.getSource().stream()
                .map(CareerDto.Info::toDto) // career create -> career Info
                .collect(Collectors.toList());


        public static ProfileDto.Info toInfoDto(Profile profile) {
            return MapperUtils.getMapper()
                .typeMap(Profile.class, ProfileDto.Info.class)
                .addMappings(mapper -> {
                    mapper.using(CAREER_LIST_TO_INFO_LIST)
                        .map(Profile::getCareers, ProfileDto.Info::setCareers);
                })
                .map(profile);
        }

        public void changeCareer(List<CareerDto.Info> careerList) {
            this.careers = careerList;
        }
    }


    @Getter
    @Builder
    @Setter
    @ApiModel(value = "ProfileDto.SearchCondition", description = "프로파일 검색 객체")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Search {

        @ApiModelProperty(value = "이름")
        private String name;
        @ApiModelProperty(value = "카테고리")
        private CategoryType categoryType;
        @ApiModelProperty(value = "카테고리 디테일")
        private CategoryDetailType categoryDetailType;
        @ApiModelProperty(value = "년차")
        private Integer year;

    }


    @Getter
    @Builder
    @Setter
    @ApiModel(value = "ProfileDto.CreateTest", description = "프로파일 생성")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTest {

        @ApiModelProperty(value = "이름")
        private String name;
        @ApiModelProperty(value = "소개")
        private String introduce;

        @ApiModelProperty(value = "이메일")
        private String email;
        @ApiModelProperty(value = "연락처")
        private String contactNumber;
        private List<Career> career;

    }

    @Getter
    @Builder
    @Setter
    @ApiModel(value = "ProfileDto.Update", description = "프로파일 t수정")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {

        @ApiModelProperty(value = "이름")
        private String name;
        @ApiModelProperty(value = "소개")
        private String introduce;

        @ApiModelProperty(value = "이메일")
        private String email;
        @ApiModelProperty(value = "연락처")
        private String contactNumber;
        private List<CareerDto.Create> career;

    }

}
