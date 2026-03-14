package com.example.MediBook.config;

import com.example.MediBook.filter.JwtAuthFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity // Kích hoạt cấu hình bảo mật web của Spring Security
@EnableMethodSecurity // Kích hoạt bảo mật ở cấp độ phương thức, cho phép sử dụng các annotation như
                      // @PreAuthorize để kiểm soát quyền truy cập vào các phương thức cụ thể trong
                      // ứng dụng
@RequiredArgsConstructor

public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    // UserDetailsService là interface của Spring Security dùng để lấy thông tin
    // user từ databse
    private final UserDetailsService userDetailsService;

    @Bean // tạo object và đưa obiect đó choSpring quản lý để các class khác dùng lại
    // SecurityFilterChain là chuổi các bảo mật cảu Spring Security, nó xác định
    // cách thức bảo vệ các endpoint của ứng dụng
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Kích hoạt CORS
                .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF vì ta dùng JWT

                // Cấu hình quản lý Session (Phiên)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Cấu hình quy tắc phân quyền cho URL
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**", // Cho phép truy cập vào các endpoint liên quan đến xác thực mà không
                                                // cần xác thực
                                "/v3/api-docs/**", // Cho phép truy cập vào tài liệu API mà không cần xác thực
                                "/swagger-ui/**" // Cho phép truy cập vào giao diện Swagger UI mà không cần xác thực
                        ).permitAll() // cho phép tất cả mọi người truy cập không cần login,token,authentication
                        .anyRequest().authenticated() // bắt buộc tất cả các URL còn lại ngoài danh sách trên
                )
                .authenticationProvider(authenticationProvider()) // Cấu hình AuthenticationProvider để xác thực người
                                                                  // dùng
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    // tạo tạo một AuthenticationProvider để:Spring Security biết cách kiểm tra
    // username + password khi người dùng login.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // tạo nv xác thực , chuyên làm việc với DAO tức là làm việc với database để lấy
        // thông tin user
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
