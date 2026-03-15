package com.example.MediBook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity // lớp này sẽ được ánh xạ thành một bảng trong cơ sở dữ liệu
@Table(name = "users")
@Data // tự động tạo getter, setter, toString, equals, hashCode
@NoArgsConstructor // tạo constructor không tham số
@AllArgsConstructor // tạo constructor với tất cả tham số
@EqualsAndHashCode(callSuper = true) // tạo equals và hashCode dựa trên các trường của lớp cha
@Builder
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class User extends BaseEntity implements UserDetails {
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    @JsonIgnore // khi trả về cho client sẽ ko bao giờ thấy trường này
    private String passwordHash;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    // Thiết lập mối quan hệ liên kết (Relationships)
    // Dùng CascadeType.ALL để khi lưu User thì tự lưu Doctor.
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Doctor doctor;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Patient patient;

    // ----- CÁC HÀM CỦA USERDETAILS CẦN IMPLEMENT -----

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Cấp quyền dựa trên enum Role
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email; // Dùng email làm tên đăng nhập
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Mặc định tài khoản không hết hạn
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive; // Khóa tài khoản nếu isActive = false
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; 
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
