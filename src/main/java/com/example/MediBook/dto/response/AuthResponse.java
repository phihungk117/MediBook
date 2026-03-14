package com.example.MediBook.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(
        String accessToken, // khai báo trường accessToken để chứa JWT token trả về cho client sau khi đăng
                            // nhập thành công
        String refreshToken,
        String email,
        String fullName,
        String role,
        long expiresIn // Trả về thời gian hết hạn (giây) để frontend đếm ngược
) {
}
