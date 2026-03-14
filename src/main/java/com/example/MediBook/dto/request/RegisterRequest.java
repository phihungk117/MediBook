package com.example.MediBook.dto.request;

import com.example.MediBook.entity.Role; // Giả sử bạn có Enum Role
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Email không được để trống") @Email(message = "Email không hợp lệ") String email,

        @NotBlank(message = "Password không được để trống") @Size(min = 6, max = 50, message = "Password từ 6-50 ký tự") String password,

        @NotBlank(message = "Họ tên không được để trống") String fullName,

        // Số điện thoại có thể null, nhưng nếu điền thì chỉ check format (tùy nghiệp
        // vụ)
        String phone,

        @NotNull(message = "Role không được để trống") Role role) {
}
