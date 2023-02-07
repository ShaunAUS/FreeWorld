package com.example.testSecurity.domain.dto.article;

import com.example.testSecurity.domain.enums.CategoryType;
import com.example.testSecurity.domain.entity.Article;
import com.example.testSecurity.domain.entity.Profile;
import com.example.testSecurity.domain.utils.MapperUtils;
import io.swagger.annotations.ApiModel;
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


    public Article toEntity() {
        return MapperUtils.getMapper()
            .typeMap(ArticleCreateDto.class, Article.class)
            .addMappings(mapper -> {
                mapper.using(CategoryType.CATEGORY_TYPE_INTEGER_CONVERTER)
                    .map(ArticleCreateDto::getCategory, Article::setCategory);
            })
            .map(this);
    }

    public void changeWriter(Profile profileByMemberNo) {
        this.changeProfile(profileByMemberNo);
    }
}
