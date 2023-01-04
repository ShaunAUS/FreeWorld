package com.example.testSecurity.repository;

import com.example.testSecurity.entity.Profile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProfileJpaRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByMemberNo(Long loginMemberNo);
}
