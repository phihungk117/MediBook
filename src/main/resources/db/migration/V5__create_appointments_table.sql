CREATE TYPE appointment_status AS ENUM
    ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED');

CREATE TABLE appointments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE RESTRICT,
    doctor_id UUID NOT NULL REFERENCES doctors(id) ON DELETE RESTRICT,
    schedule_id UUID NOT NULL REFERENCES schedules(id) ON DELETE RESTRICT,
    
    appointment_date DATE NOT NULL,
    start_time TIME NOT NULL,
    status appointment_status DEFAULT 'PENDING',
    reason TEXT,
    notes TEXT,
    cancel_reason TEXT,
    
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ
);

-- Index tối ưu cho các màn hình truy vấn phổ biến:
-- 1. Xem lịch sử khám của một bệnh nhân
CREATE INDEX idx_appointments_patient ON appointments(patient_id) WHERE deleted_at IS NULL;

-- 2. Xem lịch hẹn của bác sĩ để hệ thống nhắc việc
CREATE INDEX idx_appointments_doctor ON appointments(doctor_id) WHERE deleted_at IS NULL;

-- 3. Quét các lịch hẹn sắp tới của toàn bệnh viện để gửi email/SMS
CREATE INDEX idx_appointments_date ON appointments(appointment_date) WHERE deleted_at IS NULL AND status = 'CONFIRMED';
