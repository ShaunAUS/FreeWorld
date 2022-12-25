package com.example.testSecurity.querydlsRepository.impl;

import com.example.testSecurity.dto.ImageInfo;
import com.example.testSecurity.entity.Profile;
import com.example.testSecurity.querydlsRepository.ProfileImageCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;

import static com.example.testSecurity.entity.QProfileImage.profileImage;

public class ProfileImageRepositoryImpl implements ProfileImageCustomRepository {


    private final JPAQueryFactory queryFactory;

    public ProfileImageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public void insert(String url, Profile profileById, int countImage) {
        queryFactory.insert(profileImage)
            .set(profileImage.imageUrl, url)
            .set(profileImage.imageOrder, countImage)
            .set(profileImage.profile, profileById)
            .execute();
    }

    @Override
    public int countCurrentImages(Long profileNo) {
        return (int) queryFactory.select()
            .from(profileImage)
            .where(profileImage.profile.no.eq(profileNo))
            .fetchCount();

    }
}
