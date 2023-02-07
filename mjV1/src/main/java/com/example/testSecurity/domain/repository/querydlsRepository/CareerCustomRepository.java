package com.example.testSecurity.domain.repository.querydlsRepository;

import com.example.testSecurity.domain.entity.Career;
import java.util.List;

public interface CareerCustomRepository {

    List<Career> findByProfileNo(Long profileNo);
}
