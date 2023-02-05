package com.example.testSecurity.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import lombok.ToString;
import lombok.ToString.Include;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class MemberArticleBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_article_bookmark_no")
    private Long no;

    @ApiModelProperty(value = "프로파일 no")
    @OneToOne
    @JoinColumn(name = "profile_no")
    private Profile profile;
    @ApiModelProperty(value = "게시글 no")
    @OneToOne
    @JoinColumn(name = "article_no")
    private Article article;

}
