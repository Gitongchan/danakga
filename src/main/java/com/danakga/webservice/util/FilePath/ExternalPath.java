package com.danakga.webservice.util.FilePath;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ExternalPath implements WebMvcConfigurer {

    @Value("${file.path}") private String pathTest;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
                //파일 요청 경로
        registry.addResourceHandler("http://localhost:8081/")
                //실제 파일이 존재하는 외부 경로
                .addResourceLocations(pathTest);
    }
}
