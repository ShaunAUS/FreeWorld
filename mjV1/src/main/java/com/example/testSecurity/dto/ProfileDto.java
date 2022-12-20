package com.example.testSecurity.dto;

import com.example.testSecurity.Enum.CategoryDetailType;
import com.example.testSecurity.Enum.CategoryType;
import com.example.testSecurity.entity.Career;
import com.example.testSecurity.entity.Profile;
import com.example.testSecurity.utils.MapperUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
        @ApiModelProperty(value = "사진")
        private MultipartFile image;
        @ApiModelProperty(value = "이메일")
        private String email;
        @ApiModelProperty(value = "연락처")
        private String contactNumber;
        private List<Career> career;

        public static Profile toEntity(ProfileDto.Create profileCreateDTO) {
            return MapperUtils.getMapper()
                    .typeMap(ProfileDto.Create.class, Profile.class)
                    //TODO List
                    .map(profileCreateDTO);
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
        private List<Career> career;

        public static ProfileDto.Info toDto(Profile saveedProfile) {
            return MapperUtils.getMapper()
                    .typeMap(Profile.class, ProfileDto.Info.class)
                    .map(saveedProfile);
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
        private Integer annual;

    }

}
