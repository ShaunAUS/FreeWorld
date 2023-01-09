package com.example.testSecurity.querydlsRepository;

import com.example.testSecurity.dto.CareerDto;
import com.example.testSecurity.dto.CareerDto.Create;
import com.example.testSecurity.entity.Career;
import java.util.List;

public interface CareerCustomRepository {

    List<Career> findByProfileNo(Long profileNo);
}
