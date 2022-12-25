package com.example.testSecurity.repository;

import com.example.testSecurity.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CareerJpaRepository extends JpaRepository<Career, Long> {

}
