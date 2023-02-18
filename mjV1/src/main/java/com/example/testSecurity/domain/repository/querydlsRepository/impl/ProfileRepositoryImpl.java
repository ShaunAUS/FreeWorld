package com.example.testSecurity.domain.repository.querydlsRepository.impl;

import com.example.testSecurity.domain.enums.CategoryDetailType;
import com.example.testSecurity.domain.enums.CategoryType;
import com.example.testSecurity.domain.dto.profile.ProfileCreateDto;
import com.example.testSecurity.domain.dto.profile.ProfileSearchConditionDto;
import com.example.testSecurity.domain.entity.Profile;
import com.example.testSecurity.domain.repository.querydlsRepository.ProfileCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.testSecurity.domain.entity.QProfile.profile;
import static com.example.testSecurity.domain.entity.QCareer.career;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileCustomRepository {

    private final JPAQueryFactory queryFactory;


    //Profile 검색은 (년차)와 (카테고리(career테이블)로 검색한다
    @Override
    public Page<Profile> search(ProfileSearchConditionDto profileSearchConditionDto,
        Pageable pageable) {

        List<Profile> result = queryFactory
            .select(profile)
            .from(profile)
            .leftJoin(career)
            .on(profile.no.eq(career.profile.no))
            .where(
                containSearchYear(profileSearchConditionDto.getExperienceYear()),
                containSearchCategory(profileSearchConditionDto.getCategoryType()),
                //from career table
                containSearchCategoryDetailType(profileSearchConditionDto.getCategoryDetailType())
                //from career table
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
            .where(
                containSearchYear(profileSearchConditionDto.getExperienceYear()),
                containSearchCategory(profileSearchConditionDto.getCategoryType()),
                containSearchCategoryDetailType(profileSearchConditionDto.getCategoryDetailType())
            );

        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery.fetchOne());

    }


    //TODO profile + carrer  한방쿼리 방법 DTO에 넣는게 잘안됌,,, or  일단 따로따로 해서 넣는거로 처리
    @Override
    public ProfileCreateDto findProfileByIdWithCareer(Long profileNo) {

        return queryFactory
            .select(Projections.bean(ProfileCreateDto.class,
                profile.name,
                profile.email,
                profile.contactNumber,
                profile.introduce))
            .from(profile)
            .where(profile.no.eq(profileNo))
            .fetchOne();
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

    private BooleanExpression containSearchYear(Integer year) {

        if (year != null) {
            return profile.experienceYear.eq(year);
        }
        return null;
    }

    private BooleanExpression containSearchCategory(CategoryType categoryType) {
        if (categoryType == null) {
            return null;
        }

        Integer categoryTypeNumber = categoryType.getNumber();
        return hasText(String.valueOf(categoryTypeNumber)) ? career.category.eq(
            categoryTypeNumber) : null;
    }
}
