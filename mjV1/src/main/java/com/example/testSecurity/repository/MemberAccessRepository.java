package com.example.testSecurity.repository;

import com.example.testSecurity.auth.entity.MemberAccess;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MemberAccessRepository extends JpaRepository<MemberAccess, Long> {

    Optional<MemberAccess> findByIdAndRefreshToken(Long accessId, String refreshToken);

}

