package com.example.testSecurity.querydlsRepository.impl;

import com.example.testSecurity.Enum.CategoryType;
import com.example.testSecurity.dto.ArticleDto;
import com.example.testSecurity.entity.Article;
import com.example.testSecurity.entity.Profile;
import com.example.testSecurity.querydlsRepository.ArticleCustomRepository;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.example.testSecurity.entity.QArticle.article;
import static com.example.testSecurity.entity.QProfile.profile;
import static org.springframework.util.StringUtils.*;

public class ArticleRepositoryImpl implements ArticleCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ArticleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ArticleDto.Info> search(ArticleDto.Search articleSearchConditionDto, Pageable pageable) {

        List<ArticleDto.Info> result = queryFactory
                .select(Projections.fields(ArticleDto.Info.class,
                        profile.name.as("writer"),
                        article.title,
                        article.contents,
                        article.likeCnt,
                        article.views,
                        article.category))
                .from(article)
                .leftJoin(profile)
                .on(article.profile.no.eq(profile.no))
                .where(titleContains(articleSearchConditionDto.getTitle()),
                        contentContains(articleSearchConditionDto.getContents()),
                        categoryContains(articleSearchConditionDto.getCategory()))

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        //count쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(article.count())
                .from(article)
                .leftJoin(profile)
                .on(article.profile.no.eq(profile.no))
                .where(titleContains(articleSearchConditionDto.getTitle()),
                        contentContains(articleSearchConditionDto.getContents()),
                        categoryContains(articleSearchConditionDto.getCategory()));


        return PageableExecutionUtils.getPage(result,pageable,() -> countQuery.fetchOne());
    }


    @Override
    public void likeArticle(Long articleNo) {
        queryFactory
                .update(article)
                .set(article.likeCnt,article.likeCnt.add(1))
                .where(article.no.eq(articleNo))
                .execute();
    }

    @Override
    public Optional<Article> checkIsMemberArticle(Long articleNo, Integer loginMemberNo) {

        return Optional.ofNullable(queryFactory
                .select(article)
                .from(article)
                .where(article.profile.no.eq(
                        (Expression<? super Long>) JPAExpressions
                                .select(profile)
                                .from(profile)
                                .where(profile.member.no.eq(Long.valueOf(loginMemberNo)))
                                .fetch()
                                .stream()
                                .map(Profile::getNo)
                )).fetchOne());
    }

    BooleanExpression titleContains(String title) {
        return hasText(title) ? article.title.eq(title): null;
    }
    BooleanExpression contentContains(String contents){
        return hasText(contents) ? article.contents.eq(contents):null;
    }
    BooleanExpression categoryContains(CategoryType categoryType){
        Integer categoryInteger = CategoryType.toInteger(categoryType);
        return hasText(String.valueOf(categoryType)) ? article.category.eq(categoryInteger):null;
    }

}
