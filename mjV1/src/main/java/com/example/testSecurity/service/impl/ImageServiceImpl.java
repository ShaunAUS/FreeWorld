package com.example.testSecurity.service.impl;

import com.example.testSecurity.config.ImageConfig;
import com.example.testSecurity.dto.ImageInfo;
import com.example.testSecurity.exception.ServiceProcessException;
import com.example.testSecurity.exception.enums.ServiceMessage;
import com.example.testSecurity.service.ImageService;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageConfig config;

    @Override
    public List<ImageInfo> uploadImage(List<MultipartFile> imageList, Long profileNo) {

        List<ImageInfo> mediaInfoList = new ArrayList<>();
        for (MultipartFile multipartFile : imageList) {
            //업로드 이름
            String originalFilename = multipartFile.getOriginalFilename();

            //확장자 ex) jpg
            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));

            //서버저장이름 = 랜덤이름 + 확장자명
            String storeFilename = UUID.randomUUID().toString() + ext;

            //path에 랜덤이름으로 저장
            File location = new File(getStorageFilePath(storeFilename));

            try {
                //로컬 기본저장위치에 저장
                multipartFile.transferTo(location);
            } catch (IOException e) {
                throw new ServiceProcessException(ServiceMessage.IMAGE_LOAD_ERROR);
            }

            mediaInfoList.add(ImageInfo.builder()
                .imageNo(1L)
                .url(config.getPath() + "/" + storeFilename).build());

        }
        return mediaInfoList;

    }

    //로컬 기본 저장 위치
    private String getStorageFilePath(String filename) {
        return System.getProperty("java.io.tmpdir") + "/" + filename;
    }

    @Override
    public Resource load(String filename) {
        var loadFilename = getStorageFilePath(filename);

        try {
            Path file = Path.of(loadFilename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ServiceProcessException(ServiceMessage.IMAGE_LOAD_ERROR);
            }
        } catch (MalformedURLException e) {
            throw new ServiceProcessException(ServiceMessage.IMAGE_LOAD_ERROR);
        }
    }
}

