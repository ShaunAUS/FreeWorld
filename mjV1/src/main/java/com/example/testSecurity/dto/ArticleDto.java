package com.example.testSecurity.dto;

import com.example.testSecurity.Enum.CategoryType;
import com.example.testSecurity.entity.Career;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        private Integer likeCnt ;
        @ApiModelProperty(value = "조회수")
        private Integer views ;
        @ApiModelProperty(value = "카테고리")
        private CategoryType category;
    }


    @Getter
    @Builder
    @Setter
    @ApiModel(value = "ArticleDto.SearchCondition", description = "게시글 검색조건")
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
