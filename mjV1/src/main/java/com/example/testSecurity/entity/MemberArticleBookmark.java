package com.example.testSecurity.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberArticleBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_article_bookmark_no")
    private Long no;

    @ApiModelProperty(value = "멤버 no")
    @OneToOne
    @JoinColumn(name = "member_no")
    private Member member;
    @ApiModelProperty(value = "게시글 no")
    @OneToOne
    @JoinColumn(name = "article_no")
    private Article article;

}
