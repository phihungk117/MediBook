CREATE TABLE attachments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    medical_record_id UUID NOT NULL REFERENCES medical_records(id) ON DELETE RESTRICT,
    uploaded_by UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    
    file_name VARCHAR(255) NOT NULL,
    
    -- Lưu URL của AWS S3 hoặc MinIO, tuyệt đối không lưu file dưới dạng binary trong DB!
    file_url VARCHAR(500) NOT NULL, 
    
    -- Đổi tên thành file_size_bytes cho rõ ràng đơn vị, thêm ràng buộc số dương
    file_size_bytes BIGINT NOT NULL CHECK (file_size_bytes >= 0),
    
    -- MIME Type (vd: application/pdf, image/jpeg, image/png)
    file_type VARCHAR(100) NOT NULL, 
    
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ
);

CREATE INDEX idx_attachments_record ON attachments(medical_record_id) WHERE deleted_at IS NULL;
