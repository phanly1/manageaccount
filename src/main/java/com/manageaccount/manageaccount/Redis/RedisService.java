package com.manageaccount.manageaccount.Redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();
    // Lưu giá trị vào Redis

    public <T> void setValue(String key, T value) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value); // Chuyển đổi thành JSON
            redisTemplate.opsForValue().set(key, jsonValue, 10, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new RuntimeException("Error converting value to JSON", e);
        }
    }

    // Lấy giá trị từ Redis
    public <T> T getValue(String key, Class<T> clazz) {
        try {
            String jsonValue = redisTemplate.opsForValue().get(key);
            if (jsonValue != null) {
                return objectMapper.readValue(jsonValue, clazz); // Chuyển đổi lại thành đối tượng
            }
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to object", e);
        }
        return null;
    }

    // Xóa giá trị khỏi Redis
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
}