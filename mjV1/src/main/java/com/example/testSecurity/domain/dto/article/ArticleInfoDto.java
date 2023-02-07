package com.example.testSecurity.domain.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@Setter
@ApiModel(description = "게시글 Info DTO")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ArticleInfoDto extends ArticleUpdateDto {

    @ApiModelProperty(value = "no")
    private String no;

}
