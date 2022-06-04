package com.danakga.webservice.util.FilePath;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ExternalPath implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("")
                //실제 파일이 존재하는 외부 경로
                .addResourceLocations(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files");
    }
}
