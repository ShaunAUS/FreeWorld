package com.example.testSecurity.domain.entity;

import com.example.testSecurity.domain.dto.article.ArticleCreateDto;
import com.example.testSecurity.domain.enums.CategoryType;
import com.example.testSecurity.domain.dto.article.ArticleInfoDto;
import com.example.testSecurity.domain.dto.article.ArticleUpdateDto;
import com.example.testSecurity.domain.utils.MapperUtils;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

//게시글
@Entity
@Getter
@Setter
@NoArgsConstructor()
@ToString
@DynamicInsert
public class Article extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_no")
    private Long no;

    @ApiModelProperty(value = "제목")
    @Column(nullable = false)
    private String title;
    @ApiModelProperty(value = "내용")
    @Column(nullable = false)
    private String contents;
    @ApiModelProperty(value = "좋아요")
    private Integer likeCnt;
    @ApiModelProperty(value = "조회수")
    private Integer views;
    @ApiModelProperty(value = "카테고리")
    @Column(nullable = false)
    private Integer category;
    @ApiModelProperty(value = "카테고리 상세")
    @Column(nullable = false)
    private Integer categoryDetail;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_no")
    private Profile profile;


    public static Article of(ArticleCreateDto articleCreateDto) {
        return MapperUtils.getMapper()
            .typeMap(ArticleCreateDto.class, Article.class)
            .addMappings(mapper -> {
                mapper.using(CategoryType.CATEGORY_TYPE_INTEGER_CONVERTER)
                    .map(ArticleCreateDto::getCategory, Article::setCategory);
            })
            .map(articleCreateDto);
    }


    public void update(ArticleUpdateDto articleUpdateDto) {
        MapperUtils.getMapper()
            .typeMap(ArticleUpdateDto.class, Article.class)
            .addMappings(mapper -> {
                mapper.using(CategoryType.CATEGORY_TYPE_INTEGER_CONVERTER)
                    .map(ArticleUpdateDto::getCategory, Article::setCategory);
            })
            .map(articleUpdateDto, this);
    }

    @PrePersist
    public void prePersist() {
        this.views = this.views == null ? 0 : this.views;
        this.likeCnt = this.likeCnt == null ? 0 : this.likeCnt;
    }


    public void addLike() {
        this.likeCnt += 1;
    }

    public void addView() {
        this.views += 1;
    }
}
