package com.example.testSecurity.domain.repository;

import com.example.testSecurity.domain.entity.Career;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerJpaRepository extends JpaRepository<Career, Long> {

    List<Career> findByProfileNo(Long profileNo);


}
