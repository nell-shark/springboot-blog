package com.nellshark.springbootblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.nellshark.springbootblog.service.FileService.STORAGE_LOCATION;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/storage/**")
                .addResourceLocations("file:" + STORAGE_LOCATION + "/");
    }
}
