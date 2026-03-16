package com.example.MediBook.controller;

import com.example.MediBook.dto.request.LoginRequest;
import com.example.MediBook.dto.request.RegisterRequest;
import com.example.MediBook.dto.response.ApiResponse;
import com.example.MediBook.dto.response.AuthResponse;
import com.example.MediBook.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Đánh dấu lớp này là Controller, mọi hàm trả về đều được biến thành JSON tự động
@RequestMapping("/api/auth") // Đặt cái biển hiệu trước quán: "Khu vực Xác Thực"
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService; // "Gọi Đầu Bếp ra đây"

    @PostMapping("/register") // Khách tới quầy đăng ký
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request // @Valid: Yêu cầu Spring kiểm tra @NotBlank, @Email trước khi cho vào
    ) {
        // 1. Giao cho bếp trưởng làm
        AuthResponse responseData = authService.register(request);

        // 2. Bưng khay (ApiResponse) ra cho khách với status code 201 (Created - Đã lưu thành công)
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Đăng ký tài khoản thành công", responseData));
    }

    @PostMapping("/login") // Khách tới quầy đăng nhập
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthResponse responseData = authService.login(request);

        // Bưng khay ra cho khách với status code 200 (OK)
        return ResponseEntity.ok(
                ApiResponse.success("Đăng nhập thành công", responseData)
        );
    }
}
