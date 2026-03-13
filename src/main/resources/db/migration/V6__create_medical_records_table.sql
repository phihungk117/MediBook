CREATE TABLE medical_records (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    
    -- Một cuộc hẹn khám CHỈ có duy nhất 1 hồ sơ bệnh án, nên phải có UNIQUE
    appointment_id UUID UNIQUE NOT NULL REFERENCES appointments(id) ON DELETE RESTRICT,
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE RESTRICT,
    doctor_id UUID NOT NULL REFERENCES doctors(id) ON DELETE RESTRICT,
    
    diagnosis TEXT NOT NULL,
    prescription TEXT,
    notes TEXT,
    follow_up_date DATE,
    
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ
);

-- Index để tìm tất cả bệnh án của một bệnh nhân (rất hay dùng)
CREATE INDEX idx_medical_records_patient ON medical_records(patient_id) WHERE deleted_at IS NULL;
