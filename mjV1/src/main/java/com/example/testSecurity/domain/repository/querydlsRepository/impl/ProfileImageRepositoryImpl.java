package com.example.testSecurity.domain.repository.querydlsRepository.impl;

import static com.example.testSecurity.domain.entity.QProfileImage.profileImage;

import com.example.testSecurity.domain.repository.querydlsRepository.ProfileImageCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class ProfileImageRepositoryImpl implements ProfileImageCustomRepository {


    private final JPAQueryFactory queryFactory;
    

    @Override
    public int countCurrentImages(Long profileNo) {
        return (int) queryFactory.select()
            .from(profileImage)
            .where(profileImage.profile.no.eq(profileNo))
            .fetchCount();

    }
}
