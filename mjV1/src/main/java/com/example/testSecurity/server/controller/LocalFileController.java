package com.example.testSecurity.server.controller;

import com.example.testSecurity.service.service.ImageService;
import io.swagger.annotations.Api;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 임시로 local 저장한 이미지를 서빙하는 컨트롤러
 */
@Api(tags = "임시 파일 컨트룰러")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class LocalFileController {

    private final ImageService imageService;


    //conusme 클라가 -> 서버에게 보내는 데이터타입 강제
    //produce 서버 -> 클라에게 보내는 타입 강제
    @GetMapping(value = "/images/{filename:.+}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> view(@PathVariable String filename) throws IOException {
        Resource file = imageService.load(filename);
        return ResponseEntity.ok().body(new InputStreamResource(file.getInputStream()));
    }

}
