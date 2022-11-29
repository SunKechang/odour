package com.bjfu.li.odour.config;

import com.bjfu.li.odour.common.interceptor.JWTInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Value("${localImgPath}")
    private String localImgPath;

    @Value("${staticAccessPath}")
    private String staticAccessPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(staticAccessPath).addResourceLocations("file:" + localImgPath);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry)  {
        registry.addInterceptor(new JWTInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/register1")
                .excludePathPatterns("/user/send_email/*")
                .excludePathPatterns("/user/update/password")
                .excludePathPatterns("/compound/news")
                .excludePathPatterns("/compound/all")
                .excludePathPatterns("/oil/all")
                .excludePathPatterns("/oil/searchoil")
                .excludePathPatterns("/compound/search")
                .excludePathPatterns("/compound/advanced")
                .excludePathPatterns("/team/news")
                .excludePathPatterns("/city/citySN")
                .excludePathPatterns("/compound/{id}")
                .excludePathPatterns("/compound/downloadTemplate")
                .excludePathPatterns("/oil/{id}")
                .excludePathPatterns("/product/news")
                .excludePathPatterns("/product/{id}")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/article/getFile")
                .excludePathPatterns("/base/*");
    }
}