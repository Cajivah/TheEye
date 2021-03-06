package com.theeye.api.v1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CORSConfig {

     @Bean
     public WebMvcConfigurer corsConfigurer() {
          return new WebMvcConfigurerAdapter() {
               @Override
               public void addCorsMappings(CorsRegistry registry) {
                    registry
                            .addMapping("/**")
                            .allowedOrigins("*")
                            .allowedMethods(
                                    HttpMethod.DELETE.name(),
                                    HttpMethod.PUT.name(),
                                    HttpMethod.POST.name(),
                                    HttpMethod.GET.name());
               }
          };
     }
}
