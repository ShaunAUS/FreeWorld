package com.example.testSecurity.domain.repository;


import com.example.testSecurity.domain.entity.auth.MemberAccess;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAccessRepository extends JpaRepository<MemberAccess, Long> {

    Optional<MemberAccess> findByIdAndRefreshToken(Long accessId, String refreshToken);

}

