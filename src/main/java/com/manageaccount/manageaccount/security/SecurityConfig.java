package com.manageaccount.manageaccount.security;

//import com.manageaccount.manageaccount.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Tắt CSRF
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("http://localhost:3300"));  // Cho phép frontend từ http://localhost:3000
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));  // Các phương thức HTTP cho phép
                    config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));  // Các header cho phép
                    config.setAllowCredentials(true);  // Cho phép cookies
                    return config;
                }))  // Cấu hình CORS
                .authorizeRequests()
                .requestMatchers("/accounts/accountId","/api/login", "/api/auth/login").permitAll()  // Cho phép không cần xác thực
                .anyRequest().authenticated()  // Yêu cầu xác thực cho các yêu cầu khác
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // Đăng ký filter kiểm tra JWT

        return http.build();
    }

}
