package com.example.testSecurity.querydlsRepository.impl;

import com.example.testSecurity.Enum.CategoryDetailType;
import com.example.testSecurity.Enum.CategoryType;
import com.example.testSecurity.dto.ProfileDto;
import com.example.testSecurity.entity.Profile;
import com.example.testSecurity.querydlsRepository.ProfileCustomRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.testSecurity.entity.QProfile.profile;
import static com.example.testSecurity.entity.QCareer.career;

@Repository
public class ProfileRepositoryImpl implements ProfileCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ProfileRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ProfileDto.Info> search(ProfileDto.Search profileSearchConditionDto,
        Pageable pageable) {

        List<ProfileDto.Info> result = queryFactory
            .select(Projections.fields(ProfileDto.Info.class,
                profile.name,
                profile.introducing,
                profile.email,
                profile.contactNumber
            ))
            .from(profile)
            .leftJoin(career)
            .on(profile.no.eq(career.profile.no))
            .where(containSearchName(profileSearchConditionDto.getName()),
                containsearchCategory(profileSearchConditionDto.getCategoryType()),
                containSearchCategoryDetailType(profileSearchConditionDto.getCategoryDetailType()),
                containSearchYear(profileSearchConditionDto.getYear())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        //count query
        JPAQuery<Long> countQuery = queryFactory
            .select(profile.count())
            .from(profile)
            .leftJoin(career)
            .on(profile.no.eq(career.profile.no))
            .where(containSearchName(profileSearchConditionDto.getName()),
                containsearchCategory(profileSearchConditionDto.getCategoryType()),
                containSearchCategoryDetailType(profileSearchConditionDto.getCategoryDetailType()),
                containSearchYear(profileSearchConditionDto.getYear())
            );

        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery.fetchOne());

    }

    private BooleanExpression containSearchYear(Integer year) {
        return StringUtils.hasText(String.valueOf(year)) ? career.year.eq(year) : null;
    }

    private BooleanExpression containSearchCategoryDetailType(
        CategoryDetailType categoryDetailType) {
        Integer categoryDetailTypeNumber = categoryDetailType.getNumber();
        return StringUtils.hasText(String.valueOf(categoryDetailTypeNumber))
            ? career.categoryDetail.eq(categoryDetailTypeNumber) : null;
    }

    private BooleanExpression containsearchCategory(CategoryType categoryType) {
        Integer categoryTypeNumber = categoryType.getNumber();
        return StringUtils.hasText(String.valueOf(categoryTypeNumber)) ? career.category.eq(
            categoryTypeNumber) : null;
    }

    private BooleanExpression containSearchName(String name) {
        return StringUtils.hasText(name) ? profile.name.eq(name) : null;
    }
}
