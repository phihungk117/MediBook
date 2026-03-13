package com.example.MediBook.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "doctors")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE doctors SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Doctor extends BaseEntity {
    // Khai báo mối quan hệ
    // Đây là bên giữ khóa ngoại (Foreign Key) "user_id" trực tiếp
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    // Thêm các trường đặc thù của bác sĩ
    @Column(nullable = false)
    private String specialty; // Chuyên khoa

    @Column(name = "license_number", nullable = false, unique = true)
    private String licenseNumber; // Số giấy phép hành nghề

    @Column(name = "experience_years")
    @Builder.Default
    private Integer experienceYears = 0; // Số năm kinh nghiệm

    private String bio;

    @Column(name = "consultation_fee") // Phí tư vấn
    @Builder.Default
    private Long consultationFee = 0L;

    private String hospital; // Bệnh viện công tác

}
