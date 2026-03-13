package com.example.MediBook.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;


@MappedSuperclass //báo đừng tạo bảng cho lớp này ,mà hãy lấy các trường của nó để tạo bảng cho những class con kế thừa
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) //sử dụng UUID làm khóa chính
    //báo DB cột này ép kiểu UUID,ko cho phép cập nhật,ko bao giờ để trống
    @Column(columnDefinition = "uuid",updatable = false,nullable = false)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
