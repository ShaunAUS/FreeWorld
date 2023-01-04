package com.example.testSecurity.entity;

import com.example.testSecurity.Enum.CategoryType;
import com.example.testSecurity.Enum.RoleType;
import com.example.testSecurity.dto.ArticleDto;
import com.example.testSecurity.dto.MemberDto;
import com.example.testSecurity.utils.MapperUtils;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.transaction.Transactional;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//게시글
@Entity
@Getter
@Setter
@NoArgsConstructor()
public class Article extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_no")
    private Long no;

    @ApiModelProperty(value = "작성자") // = ProfileName?
    private String writer;

    @ApiModelProperty(value = "제목")
    private String title;
    @ApiModelProperty(value = "내용")
    private String contents;
    @ApiModelProperty(value = "좋아요")
    private Integer likeCnt;
    @ApiModelProperty(value = "조회수")
    private Integer views;
    @ApiModelProperty(value = "카테고리")
    private Integer category;
    @ApiModelProperty(value = "카테고리 상세")
    private Integer categoryDetail;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_no")
    private Profile profile;

    public static ArticleDto.Info toDto(Article article) {
        return MapperUtils.getMapper()
            .typeMap(Article.class, ArticleDto.Info.class)
            .addMappings(mapper -> {
                mapper.using(CategoryType.INTEGER_CATEGORY_TYPE_CONVERTER)
                    .map(Article::getCategory, ArticleDto.Info::setCategory);
            })
            .map(article);
    }


    public static void update(Article article, ArticleDto.Create articleCreateDTO) {
        MapperUtils.getMapper()
            .typeMap(ArticleDto.Create.class, Article.class)
            .addMappings(mapper -> {
                mapper.using(CategoryType.CATEGORY_TYPE_INTEGER_CONVERTER)
                    .map(ArticleDto.Create::getCategory, Article::setCategory);
            })
            .map(articleCreateDTO, article);
    }


    public void addLike() {
        this.likeCnt += 1;
    }


    //TODO need to check, so inefficient
    public Article chageProfile(Profile profile) {
        this.profile = profile;
        return this;
    }

    public void addView() {
        this.views += 1;
    }
}
