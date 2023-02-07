package com.example.testSecurity.querydlsRepository.impl;

import static com.example.testSecurity.entity.QCareer.career;

import com.example.testSecurity.entity.Career;
import com.example.testSecurity.querydlsRepository.CareerCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class CareerRepositoryImpl implements CareerCustomRepository {

    private final JPAQueryFactory queryFactory;

    public CareerRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

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
