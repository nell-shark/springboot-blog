package com.nellshark.springbootblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.nellshark.springbootblog.utils.FileUtils.APP_LOCATION;
import static com.nellshark.springbootblog.utils.FileUtils.STORAGE_FOLDER;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/storage/**")
                .addResourceLocations("file:" + APP_LOCATION + STORAGE_FOLDER + "/");
    }
}