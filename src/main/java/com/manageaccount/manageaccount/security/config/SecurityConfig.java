import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Tắt CSRF (thường tắt khi làm việc với API)
                .authorizeRequests()
                .antMatchers("/check-security").permitAll()  // Cho phép endpoint này mà không cần xác thực
                .anyRequest().authenticated()  // Cần xác thực cho tất cả các yêu cầu khác
                .and()
                .addFilterBefore(jwtAuthenticationFilter, JwtAuthenticationFilter.class);  // Thêm filter JWT vào chuỗi bảo mật
    }
}

