package com.manageaccount.manageaccount.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Đảm bảo rằng JavaTimeModule được đăng ký
        objectMapper.findAndRegisterModules(); // Tự động tìm các module cần thiết
        return objectMapper;

    }
}