package com.example.testSecurity.domain.repository;

import com.example.testSecurity.domain.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserName(String name);

}

