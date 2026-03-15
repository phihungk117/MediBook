package com.example.MediBook.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Record class để nhận dữ liệu đăng nhập từ client
public record LoginRequest (
    //@NotBlank dùng để kiểm tra dữ liệu không được rỗng và không chỉ chứa khoảng trắng.
    @NotBlank(message = "Email không được để trống")
    @NotBlank(message = "Email không được để trống")
    String email,

    @NotBlank(message = "Password không được để trống")
    String password
) {}
