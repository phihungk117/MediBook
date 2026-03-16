package com.example.MediBook.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Chỉ serialize các trường không null
public class ApiResponse<T> {
    // <T> class này có thể dùng nhiều dữ liệu khác nhau
    private int status;
    private String message;
    private T data; // Trường data có kiểu dữ liệu linh hoạt, có thể là

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(201, message, data);
    }
}
