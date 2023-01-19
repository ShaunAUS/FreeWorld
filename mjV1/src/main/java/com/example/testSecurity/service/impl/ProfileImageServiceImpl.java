package com.example.testSecurity.service.impl;

import com.example.testSecurity.dto.ImageInfo;
import com.example.testSecurity.entity.Profile;
import com.example.testSecurity.entity.ProfileImage;
import com.example.testSecurity.exception.ServiceProcessException;
import com.example.testSecurity.exception.enums.ServiceMessage;
import com.example.testSecurity.querydlsRepository.ProfileImageCustomRepository;
import com.example.testSecurity.repository.ProfileImageJpaRepository;
import com.example.testSecurity.repository.ProfileJpaRepository;
import com.example.testSecurity.service.ImageService;
import com.example.testSecurity.service.ProfileImageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ProfileImageServiceImpl implements ProfileImageService {

    private final ImageService imageService;
    private final ProfileImageCustomRepository profileImageCustomRepository;
    private final ProfileJpaRepository profileJpaRepository;
    private final ProfileImageJpaRepository profileImageJpaRepository;


    @Override
    public List<ImageInfo> registerImage(List<MultipartFile> imageList, Long profileNo) {

        Profile profileById = profileJpaRepository.findById(profileNo).get();

        //사진 저장
        List<ImageInfo> imageInfoList = imageService.uploadImage(imageList, profileNo);

        //URL 저장
        for (ImageInfo imageInfo : imageInfoList) {

            if (imageInfo == null || imageInfo.getUrl() == null) {
                throw new ServiceProcessException(ServiceMessage.IMAGE_LOAD_ERROR);
            }

            //querydsl insert 라이브러리 에러로인한 JPARepo 사용
            ProfileImage profileImage = ProfileImage.builder()
                .imageUrl(imageInfo.getUrl())
                .imageOrder(countImages(profileNo) + 1)
                .profile(profileById)
                .build();
            profileImageJpaRepository.save(profileImage);
        }
        return imageInfoList;
    }


    @Override
    public int countImages(Long profileNo) {
        return profileImageCustomRepository.countCurrentImages(profileNo);
    }
}
