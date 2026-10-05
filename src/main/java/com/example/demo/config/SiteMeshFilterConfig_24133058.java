package com.example.demo.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SiteMeshFilterConfig_24133058 {

    @Bean
    public FilterRegistrationBean<ConfigurableSiteMeshFilter> siteMeshFilter() {
        FilterRegistrationBean<ConfigurableSiteMeshFilter> filter = new FilterRegistrationBean<>();
        
        filter.setFilter(new ConfigurableSiteMeshFilter() {
            @Override
            protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
                builder.addExcludedPath("/WEB-INF/**")
                       .addExcludedPath("/static/**")
                       .addDecoratorPath("/admin/**", "/WEB-INF/decorators/admin.jsp")
                       .addDecoratorPath("/**", "/WEB-INF/decorators/web.jsp");
            }
        });
        
        filter.addUrlPatterns("/*");
        filter.setOrder(1);
        return filter;
    }
}