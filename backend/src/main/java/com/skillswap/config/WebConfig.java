package com.skillswap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.storage.local.root-dir:uploads}")
    private String rootDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Only relevant when app.storage.provider=local (the dev default).
        // Serves whatever's under the local uploads folder at /files/**.
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + rootDir + "/");
    }
}
