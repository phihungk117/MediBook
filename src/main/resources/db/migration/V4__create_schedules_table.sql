CREATE TABLE schedules (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    doctor_id UUID NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
    schedule_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    slot_duration_minutes INT DEFAULT 30,
    max_patients INT DEFAULT 10,
    current_patients INT DEFAULT 0, -- Rất quan trọng để đếm tổng số bệnh nhân đã book lượt này
    version INT DEFAULT 1, -- Phục vụ Optimistic locking (Khoá lạc quan) chống Race Condition
    
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraint 1: Bác sĩ không thể tạo 2 lịch có cùng thời gian bắt đầu trong 1 ngày
    CONSTRAINT unq_doctor_schedule UNIQUE(doctor_id, schedule_date, start_time),
    
    -- Constraint 2: Ràng buộc tính hợp lý của thời gian
    CONSTRAINT chk_time_logic CHECK (start_time < end_time),
    
    -- Constraint 3: Chốt chặn cuối bảo vệ DB không bao giờ bị book lố
    CONSTRAINT chk_patient_limit CHECK (current_patients <= max_patients)
);

CREATE INDEX idx_schedules_search ON schedules(doctor_id, schedule_date, is_available);
