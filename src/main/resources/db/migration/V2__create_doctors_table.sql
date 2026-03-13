CREATE TABLE doctors (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID UNIQUE NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    specialty VARCHAR(255) NOT NULL,
    license_number VARCHAR(100) UNIQUE NOT NULL,
    experience_years INT DEFAULT 0 CHECK (experience_years >= 0),
    bio TEXT,
    consultation_fee BIGINT DEFAULT 0 CHECK (consultation_fee >= 0), -- Dùng BIGINT để tính toán an toàn bên Golang
    hospital VARCHAR(255),
    
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ
);

CREATE INDEX idx_doctors_specialty ON doctors(specialty) WHERE deleted_at IS NULL;
