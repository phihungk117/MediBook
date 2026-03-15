package com.example.MediBook.service;

import com.example.MediBook.dto.request.LoginRequest;
import com.example.MediBook.dto.request.RegisterRequest;
import com.example.MediBook.dto.response.AuthResponse;
import com.example.MediBook.entity.Doctor;
import com.example.MediBook.entity.Patient;
import com.example.MediBook.entity.Role;
import com.example.MediBook.entity.User;
import com.example.MediBook.repository.UserRepository;
import com.example.MediBook.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 1. Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email đã được sử dụng!");
        }

        // 2. Tạo user mới
        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .phone(request.phone())
                .role(request.role())
                .isActive(null)
                .build();

        // 3 phân loại user theo role
        if (request.role() == Role.DOCTOR) {
            Doctor doctor = Doctor.builder()
                    .user(user)
                    .specialty("chưa câp nhat") // chuyên khoa
                    .licenseNumber("TEMP-" + System.currentTimeMillis())// giấy phép hành nghề tam thời
                    .build();
            user.setDoctor(doctor);
        } else if (request.role() == Role.PATIENT) {
            Patient patient = Patient.builder()
                    .user(user)
                    .build();
            user.setPatient(patient);
        }

        // 4. Lưu vào DB
        User savedUser = userRepository.save(user);

        // 5 tạo token
        String jwtToken = jwtUtil.generateToken(savedUser, savedUser);

        // 6. Trả về response
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .role(savedUser.getRole().name())
                .build();

    }

    public AuthResponse login(LoginRequest request) {
        // 1 giao cho spring security xử lý xác thực, nếu sai sẽ tự động ném exception
        authenticationManager.authenticate(
                // UsernamePasswordAuthenticationToken là một class của Spring Security dùng để
                // chứa thông tin đăng nhập (username và password) khi thực hiện xác thực
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        // 2. Nếu xác thực thành công, lấy thông tin user từ DB
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));

        // 3 kiếm tra active
        if (!user.getIsActive()) {
            throw new IllegalStateException("Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ quản trị viên.");
        }
        // 4. Tạo token
        String jwtToken = jwtUtil.generateToken(user, user);

        // 5. Trả về response
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .build();

    }
}