package com.example.testSecurity.domain.repository;

import com.example.testSecurity.domain.entity.Profile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileJpaRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findProfileByMemberNo(Long loginMemberNo);
}
