package com.example.testSecurity.querydlsRepository;

import com.example.testSecurity.entity.Profile;

public interface ProfileImageCustomRepository {

    void insert(String url, Profile profileById, int countImage);


    int countCurrentImages(Long profileNo);
}
