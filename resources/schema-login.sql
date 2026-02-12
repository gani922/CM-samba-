-- Login records table
DROP TABLE IF EXISTS sso_user_login_record;
CREATE TABLE sso_user_login_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sap_id VARCHAR(50),
    login_email VARCHAR(255),
    company_name VARCHAR(255),
    region VARCHAR(100),
    branch VARCHAR(100),
    district VARCHAR(100),
    client_ip VARCHAR(45),
    login_time TIMESTAMP,
    logout_time TIMESTAMP,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    session_id VARCHAR(100),
    status INT DEFAULT 1
);

-- Company table
DROP TABLE IF EXISTS company;
CREATE TABLE company (
    sap_id VARCHAR(50) PRIMARY KEY,
    company_name VARCHAR(255),
    region VARCHAR(100),
    branch VARCHAR(100),
    district VARCHAR(100)
);

-- User table
DROP TABLE IF EXISTS sso_user;
CREATE TABLE sso_user (
    email VARCHAR(255) PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    company_sap_id VARCHAR(50)
);

-- Export job table
DROP TABLE IF EXISTS export_job;
CREATE TABLE export_job (
    id VARCHAR(50) PRIMARY KEY,
    user_email VARCHAR(255),
    search_criteria TEXT,
    file_path VARCHAR(500),
    file_name VARCHAR(255),
    status VARCHAR(20) DEFAULT 'PENDING',
    error_message TEXT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_time TIMESTAMP,
    expiry_time TIMESTAMP,
    download_count INT DEFAULT 0
);

-- Create indexes for login records
CREATE INDEX idx_login_email ON sso_user_login_record(login_email);
CREATE INDEX idx_login_time ON sso_user_login_record(login_time);
CREATE INDEX idx_session_id ON sso_user_login_record(session_id);
CREATE INDEX idx_export_status ON export_job(status);
CREATE INDEX idx_export_expiry ON export_job(expiry_time);