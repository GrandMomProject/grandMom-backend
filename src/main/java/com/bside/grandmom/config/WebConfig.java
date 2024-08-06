package com.bside.grandmom.config;


import com.bside.grandmom.context.AccessContextInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public WebMvcConfigurer corsConfigurer(AccessContextInterceptor accessContextInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000") // 허용할 도메인을 지정합니다.
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
                        .allowCredentials(true);
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(accessContextInterceptor);
            }
        };
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 정적 리소스 핸들러 추가
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/root/grandmom/grandmom-image/");
    }
}
