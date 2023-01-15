package com.example.testSecurity.querydlsRepository.impl;

import com.example.testSecurity.Enum.CategoryDetailType;
import com.example.testSecurity.Enum.CategoryType;
import com.example.testSecurity.dto.ProfileDto;
import com.example.testSecurity.dto.ProfileDto.Search;
import com.example.testSecurity.entity.Profile;
import com.example.testSecurity.querydlsRepository.ProfileCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.testSecurity.entity.QProfile.profile;
import static com.example.testSecurity.entity.QCareer.career;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class ProfileRepositoryImpl implements ProfileCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ProfileRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Profile> search(Search profileSearchConditionDto,
        Pageable pageable) {

        List<Profile> result = queryFactory
            .select(profile)
            .from(profile)
            .leftJoin(career)
            .on(profile.no.eq(career.profile.no))
            .where(containSearchName(profileSearchConditionDto.getName()),
                containSearchCategory(profileSearchConditionDto.getCategoryType()),
                containSearchCategoryDetailType(profileSearchConditionDto.getCategoryDetailType()),
                containSearchYear(profileSearchConditionDto.getYear())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .distinct().fetch();

        //count query
        JPAQuery<Long> countQuery = queryFactory
            .select(profile.count())
            .from(profile)
            .leftJoin(career)
            .on(profile.no.eq(career.profile.no))
            .where(containSearchName(profileSearchConditionDto.getName()),
                containSearchCategory(profileSearchConditionDto.getCategoryType()),
                containSearchCategoryDetailType(profileSearchConditionDto.getCategoryDetailType()),
                containSearchYear(profileSearchConditionDto.getYear())
            );

        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery.fetchOne());

    }

    //TODO profile + carrer  한방쿼리 방법 DTO에 넣는게 잘안됌,,, or  일단 따로따로 해서 넣는거로 처리
    @Override
    public ProfileDto.Create findProfileByIdWithCareer(Long profileNo) {

        return queryFactory
            .select(Projections.bean(ProfileDto.Create.class,
                profile.name,
                profile.email,
                profile.contactNumber,
                profile.introduce))
            .from(profile)
            .where(profile.no.eq(profileNo))
            .fetchOne();
    }

    private BooleanExpression containSearchYear(Integer year) {
        if (year == null) {
            return null;
        }
        return career.year.eq(year);

    }

    private BooleanExpression containSearchCategoryDetailType(
        CategoryDetailType categoryDetailType) {
        if (categoryDetailType == null) {
            return null;
        }

        Integer categoryDetailTypeNumber = categoryDetailType.getNumber();
        return hasText(String.valueOf(categoryDetailTypeNumber)) ? career.categoryDetail.eq(
            categoryDetailTypeNumber) : null;
    }

    private BooleanExpression containSearchCategory(CategoryType categoryType) {
        if (categoryType == null) {
            return null;
        }

        Integer categoryTypeNumber = categoryType.getNumber();
        return hasText(String.valueOf(categoryTypeNumber)) ? career.category.eq(
            categoryTypeNumber) : null;
    }

    private BooleanExpression containSearchName(String name) {
        return hasText(name) ? profile.name.eq(name) : null;
    }
}
