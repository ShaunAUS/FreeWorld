package com.example.testSecurity.repository;

import com.example.testSecurity.auth.entity.MemberAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberAccessRepository extends JpaRepository<MemberAccess, Long> {

}

