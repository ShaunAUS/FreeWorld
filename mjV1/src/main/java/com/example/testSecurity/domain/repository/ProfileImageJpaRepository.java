package com.example.testSecurity.domain.repository;

import com.example.testSecurity.domain.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageJpaRepository extends JpaRepository<ProfileImage, Long> {

}
