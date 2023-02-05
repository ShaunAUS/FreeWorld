package com.example.testSecurity.repository;

import com.example.testSecurity.entity.Profile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileJpaRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findProfileByMemberNo(Long loginMemberNo);
}
