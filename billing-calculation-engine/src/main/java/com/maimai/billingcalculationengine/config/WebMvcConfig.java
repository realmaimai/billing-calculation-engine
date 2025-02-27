package com.maimai.billingcalculationengine.config;

import com.maimai.billingcalculationengine.common.interceptor.JwtTokenInterceptor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    private JwtTokenInterceptor jwtTokenInterceptor;

    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/login",
            "/logout",
            "/register",
            "/api/v1/auth/login",
            "/api/v1/auth/signup",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/error"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("Initializing JWT interceptor with excluded paths: {}", EXCLUDE_PATHS);

        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .resourceChain(false);

    }
}