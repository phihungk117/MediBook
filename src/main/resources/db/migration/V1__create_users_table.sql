-- Kích hoạt extension để DB có thể xử lý việc tự động sinh UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE user_role AS ENUM ('ADMIN', 'DOCTOR', 'PATIENT');

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL, -- Đổi tên rõ ràng hơn là password_hash để nhắc nhở dev
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role user_role NOT NULL DEFAULT 'PATIENT',
    avatar_url VARCHAR(500),
    
    -- Status và Audit Fields (Trường kiểm toán)
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ -- Phục vụ Soft Delete
);

-- Partial Index (Chỉ mục một phần): Chỉ đánh index những user còn tồn tại
-- Giúp tăng tốc truy vấn đăng nhập và tiết kiệm dung lượng RAM/Disk cho DB
CREATE INDEX idx_users_email_active ON users(email) WHERE deleted_at IS NULL;
CREATE INDEX idx_users_role ON users(role);
