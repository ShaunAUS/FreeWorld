package com.example.testSecurity.domain.dto.article;

import com.example.testSecurity.domain.enums.CategoryType;
import com.example.testSecurity.domain.entity.Article;
import com.example.testSecurity.domain.entity.Profile;
import com.example.testSecurity.domain.utils.MapperUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@Setter
@ApiModel(description = "게시글 작성 DTO")
@NoArgsConstructor
@SuperBuilder
public class ArticleCreateDto extends ArticleUpdateDto {


    @ApiModelProperty(value = "제목")
    private String title;
    @ApiModelProperty(value = "내용")
    private String contents;
    @ApiModelProperty(value = "좋아요")
    private Integer likeCnt;
    @ApiModelProperty(value = "조회수")
    private Integer views;
    @ApiModelProperty(value = "카테고리")
    private CategoryType category;
    @ApiModelProperty(value = "프로파일")
    private Profile profile;

    public void changeWriter(Profile profileByMemberNo) {
        this.changeProfile(profileByMemberNo);
    }
}
