package com.example.testSecurity.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties("app.image.path")
public class ImageConfig {

    public final String path = "/v1/images";
}
