package com.example.testSecurity.domain.repository.querydlsRepository.impl;

import com.example.testSecurity.domain.enums.CategoryType;
import com.example.testSecurity.domain.repository.querydlsRepository.ArticleCustomRepository;
import com.example.testSecurity.domain.dto.article.ArticleSearchConditionDto;
import com.example.testSecurity.domain.entity.Article;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

import static com.example.testSecurity.domain.entity.QArticle.article;
import static com.example.testSecurity.domain.entity.QArticle.article;
import static com.example.testSecurity.domain.entity.QProfile.profile;
import static org.springframework.util.StringUtils.*;

@Repository
public class ArticleRepositoryImpl implements ArticleCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ArticleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    //게시글 검색은  키워드(제목+내용), 카테고리 로 한다
    @Override
    public Page<Article> search(ArticleSearchConditionDto searchCondition, Pageable pageable) {

        List<Article> result = queryFactory
            .select(article)
            .from(article)
            .where(
                keywordContains(searchCondition.getKeyword()),
                //contentContains(searchCondition.getKeyword()),
                categoryContains(searchCondition.getCategory())
            )

            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        //count쿼리
        JPAQuery<Long> countQuery = queryFactory
            .select(article.count())
            .from(article)
            .where(keywordContains(searchCondition.getKeyword()),
                //contentContains(searchCondition.getKeyword()),
                categoryContains(searchCondition.getCategory()));

        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery.fetchOne());
    }

    @Override
    public List<Article> checkIsMemberArticle(Long articleNo, Long loginMemberNo) {

        return queryFactory
            .select(article)
            .from(article)
            .where(article.profile.no.eq(
                JPAExpressions
                    .select(profile.no)
                    .from(profile)
                    .where(profile.member.no.eq(loginMemberNo))
            )).fetch();

    }

    //null이면 그냥 무시 됌 , 에러 x
    BooleanExpression keywordContains(String keyword) {
        return hasText(keyword) ? article.title.contains(keyword)
            .or(article.contents.contains(keyword)) : null;
    }

    BooleanExpression contentContains(String contents) {
        return hasText(contents) ? article.contents.contains(contents) : null;
    }

    BooleanExpression categoryContains(CategoryType categoryType) {
        if (categoryType == null) {
            return null;
        }
        Integer categoryInteger = CategoryType.toInteger(categoryType);
        return hasText(String.valueOf(categoryType)) ? article.category.eq(categoryInteger) : null;
    }

}
