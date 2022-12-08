package com.nellshark.springbootblog.config;

import com.nellshark.springbootblog.utils.FileUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/storage/**")
                .addResourceLocations("file:" + FileUtils.STORAGE_LOCATION);
    }
}
