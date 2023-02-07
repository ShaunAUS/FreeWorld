package com.example.testSecurity.service.service;

import com.example.testSecurity.domain.dto.ImageInfo;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageService {

    List<ImageInfo> registerImage(List<MultipartFile> imageList, Long profileNo);


    int countImages(Long profileNo);
}
