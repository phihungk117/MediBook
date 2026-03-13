package com.example.MediBook.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "schedules")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule extends BaseEntity {
    //Định nghĩa Mối quan hệ với Bác sĩ
   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    @Column(name = "slot_duration_minutes")
    @Builder.Default
    private Integer slotDurationMinutes = 30;
    @Column(name = "max_patients")
    @Builder.Default
    private Integer maxPatients = 10;
    @Column(name = "current_patients")
    @Builder.Default
    private Integer currentPatients = 0;
    @Column(name = "is_available")
    @Builder.Default
    private Boolean isAvailable = true;

    @Version
    @Builder.Default 
    private Integer version = 1;
    
}
