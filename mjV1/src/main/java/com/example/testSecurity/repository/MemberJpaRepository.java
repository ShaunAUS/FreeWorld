package com.example.testSecurity.repository;

import com.example.testSecurity.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserName(String name);

}

