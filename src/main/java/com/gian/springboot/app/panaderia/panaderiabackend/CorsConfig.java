package com.gian.springboot.app.panaderia.panaderiabackend;

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
                registry.addMapping("/**") // Enable CORS for all routes
                        .allowedOrigins("http://localhost:52236", "http://localhost:4200") // Allow both client origins
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // Allow specific HTTP methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow cookies/credentials
            }
        };
    }
}