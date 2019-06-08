//package com.liy.chat.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//import static org.springframework.web.cors.CorsConfiguration.ALL;
//
///**
// * data: 2019/6/8 20:42
// **/
//@Configuration
//public class CrosConfig {
//    @Bean
//    public WebMvcConfigurer corsConfigurer(){
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                // 限制了路径和域名的访问
//                /*registry.addMapping("/api*").allowedOrigins("http://localhost:8081");*/
//                registry.addMapping("/**")
//                        .allowedOrigins(ALL)
//                        .allowedMethods(ALL)
//                        .allowedHeaders(ALL)
//                        .allowCredentials(true);
//            }
//        };
//    }
//}
