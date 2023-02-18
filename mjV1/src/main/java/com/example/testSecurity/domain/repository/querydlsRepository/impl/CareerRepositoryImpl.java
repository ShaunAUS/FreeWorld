package com.example.testSecurity.domain.repository.querydlsRepository.impl;

import static com.example.testSecurity.domain.entity.QCareer.career;


import com.example.testSecurity.domain.repository.querydlsRepository.CareerCustomRepository;
import com.example.testSecurity.domain.entity.Career;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CareerRepositoryImpl implements CareerCustomRepository {

    private final JPAQueryFactory queryFactory;


    //career -> career.create
    @Override
    public List<Career> findByProfileNo(Long profileNo) {

        return queryFactory
            .select(career)
            .from(career)
            .where(career.profile.no.eq(profileNo))
            .fetch();

    }
}
