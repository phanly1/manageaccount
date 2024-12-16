package com.manageaccount.manageaccount.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.manageaccount.manageaccount.dto.PageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

//    @Autowired
//    @Qualifier("redisTemplate")
//    private RedisTemplate<String, Object> redisTemplate;
//
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();


    // Lưu Page vào Redis
    public <T> void setPageValue(String key, Page<T> page) {
        try {
            PageWrapper<T> pageWrapper = new PageWrapper<>(
                    page.getContent(),
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalElements(),
                    page.getTotalPages()
            );
            String jsonValue = objectMapper.writeValueAsString(pageWrapper);
            redisTemplate.opsForValue().set(key, jsonValue, 10, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new RuntimeException("Error converting Page to JSON", e);
        }
    }

    public <T> Page<T> getPageValue(String key, Class<T> clazz) {
        String jsonValue = redisTemplate.opsForValue().get(key);
        if (jsonValue != null) {
            try {
                JavaType javaType = TypeFactory.defaultInstance().constructParametricType(PageWrapper.class, clazz);
                PageWrapper<T> pageWrapper = objectMapper.readValue(jsonValue, javaType);

                // Tạo lại đối tượng Page từ PageWrapper
                return new PageImpl<>(
                        pageWrapper.getContent(),
                        PageRequest.of(pageWrapper.getPage(), pageWrapper.getSize()),
                        pageWrapper.getTotalElements()
                );
            } catch (Exception e) {
                throw new RuntimeException("Error converting JSON to object", e);
            }
        }
        return Page.empty();
    }

}
