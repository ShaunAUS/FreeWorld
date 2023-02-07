package com.example.testSecurity.domain.dto.article;

import com.example.testSecurity.domain.enums.CategoryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@ApiModel(description = "게시글 검색조건 DTO")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleSearchConditionDto {

    @ApiModelProperty(value = "제목+내용")
    private String keyword;
    @ApiModelProperty(value = "카테고리")
    private CategoryType category;


}
