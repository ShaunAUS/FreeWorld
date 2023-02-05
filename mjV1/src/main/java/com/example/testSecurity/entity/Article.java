package com.example.testSecurity.entity;

import com.example.testSecurity.Enum.CategoryType;
import com.example.testSecurity.dto.ArticleDto;
import com.example.testSecurity.utils.MapperUtils;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
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

    public ArticleDto.Info toInfoDto() {
        return MapperUtils.getMapper()
            .typeMap(Article.class, ArticleDto.Info.class)
            .addMappings(mapper -> {
                mapper.using(CategoryType.INTEGER_CATEGORY_TYPE_CONVERTER)
                    .map(Article::getCategory, ArticleDto.Info::setCategory);
            })
            .map(this);
    }


    public void update(ArticleDto.Update articleUpdateDto) {
        MapperUtils.getMapper()
            .typeMap(ArticleDto.Update.class, Article.class)
            .addMappings(mapper -> {
                mapper.using(CategoryType.CATEGORY_TYPE_INTEGER_CONVERTER)
                    .map(ArticleDto.Update::getCategory, Article::setCategory);
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
