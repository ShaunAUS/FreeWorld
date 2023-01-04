package com.example.testSecurity.dto;

import com.example.testSecurity.Enum.CategoryType;
import com.example.testSecurity.entity.Article;
import com.example.testSecurity.entity.Profile;
import com.example.testSecurity.utils.MapperUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@RequiredArgsConstructor
public class ArticleDto {

    @Getter
    @Builder
    @Setter
    @ApiModel(value = "ArticleDto.Create", description = "게시글 생성")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

        @ApiModelProperty(value = "제목")
        private String title;
        @ApiModelProperty(value = "내용")
        private String contents;
        @ApiModelProperty(value = "작성자")
        private String writer;
        @ApiModelProperty(value = "좋아요")
        private Integer likeCnt = 0;
        @ApiModelProperty(value = "조회수")
        private Integer views = 0;
        @ApiModelProperty(value = "카테고리")
        private CategoryType category;

        public static Article toEntity(ArticleDto.Create createDto, Profile profile) {
            return MapperUtils.getMapper()
                .typeMap(ArticleDto.Create.class, Article.class)
                .addMappings(mapper -> {
                    mapper.using(CategoryType.CATEGORY_TYPE_INTEGER_CONVERTER)
                        .map(ArticleDto.Create::getCategory, Article::setCategory);
                })
                .map(createDto)
                .chageProfile(profile);
        }

        public void changeWriter(String writer) {
            this.writer = writer;
        }

    }

    @Getter
    @Builder
    @Setter
    @ApiModel(value = "ArticleDto.Info", description = "게시글 정보")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {

        @ApiModelProperty(value = "제목")
        private String title;
        @ApiModelProperty(value = "내용")
        private String contents;
        @ApiModelProperty(value = "작성자")
        private String writer;
        @ApiModelProperty(value = "좋아요")
        private Integer likeCnt;
        @ApiModelProperty(value = "조회수")
        private Integer views;
        @ApiModelProperty(value = "카테고리")
        private CategoryType category;
    }


    @Getter
    @Builder
    @Setter
    @ApiModel(value = "ArticleDto.SearchCondition", description = "게시글 조건")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Search {

        @ApiModelProperty(value = "제목")
        private String title;
        @ApiModelProperty(value = "내용")
        private String contents;
        @ApiModelProperty(value = "카테고리")
        private CategoryType category;
    }

}
