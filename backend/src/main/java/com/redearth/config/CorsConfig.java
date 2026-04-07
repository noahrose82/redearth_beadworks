package com.redearth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("https://redearth-beadworks.vercel.app")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowedOriginPatterns(
    "https://redearth-beadworks.vercel.app",
    "https://redearth-beadworks-*.vercel.app"
);
            }
        };
    }
}