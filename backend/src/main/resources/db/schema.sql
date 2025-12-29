-- 劳务派遣管理平台数据库初始化脚本
CREATE DATABASE IF NOT EXISTS labor_dispatch DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE labor_dispatch;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    real_name VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    role INT DEFAULT 3 COMMENT '1-管理员 2-企业客户 3-派遣员工',
    status INT DEFAULT 1 COMMENT '0-禁用 1-启用',
    deleted INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 企业表
CREATE TABLE IF NOT EXISTS enterprise (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    name VARCHAR(100) NOT NULL,
    credit_code VARCHAR(50),
    contact_person VARCHAR(50),
    contact_phone VARCHAR(20),
    address VARCHAR(200),
    audit_status INT DEFAULT 0 COMMENT '0-待审核 1-通过 2-拒绝',
    deleted INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 员工表
CREATE TABLE IF NOT EXISTS employee (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    name VARCHAR(50) NOT NULL,
    id_card VARCHAR(20),
    gender INT COMMENT '1-男 2-女',
    birth_date DATE,
    phone VARCHAR(20),
    skills VARCHAR(500),
    expected_salary DECIMAL(10,2),
    resume_url VARCHAR(500),
    deleted INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 岗位表
CREATE TABLE IF NOT EXISTS job (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    enterprise_id BIGINT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    skills VARCHAR(500),
    salary_min DECIMAL(10,2),
    salary_max DECIMAL(10,2),
    salary_type INT COMMENT '1-时薪 2-日薪 3-月薪',
    location VARCHAR(200),
    headcount INT DEFAULT 1,
    status INT DEFAULT 1 COMMENT '0-关闭 1-招聘中',
    deleted INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 派遣记录表
CREATE TABLE IF NOT EXISTS dispatch (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT,
    job_id BIGINT,
    enterprise_id BIGINT,
    status INT DEFAULT 1 COMMENT '1-面试邀约 2-录用 3-签约 4-入职 5-在职 6-离职 7-退回',
    start_date DATE,
    end_date DATE,
    remark VARCHAR(500),
    deleted INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 考勤表
CREATE TABLE IF NOT EXISTS attendance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dispatch_id BIGINT,
    employee_id BIGINT,
    work_date DATE,
    work_hours DECIMAL(4,2),
    status INT COMMENT '1-正常 2-迟到 3-早退 4-缺勤',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 合同表
CREATE TABLE IF NOT EXISTS contract (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dispatch_id BIGINT,
    employee_id BIGINT,
    enterprise_id BIGINT,
    contract_no VARCHAR(50),
    contract_type INT COMMENT '1-劳务派遣合同 2-用工协议',
    start_date DATE,
    end_date DATE,
    file_url VARCHAR(500),
    status INT DEFAULT 0 COMMENT '0-待签署 1-已签署 2-已终止',
    deleted INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 薪资表
CREATE TABLE IF NOT EXISTS salary (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dispatch_id BIGINT,
    employee_id BIGINT,
    year INT,
    month INT,
    base_salary DECIMAL(10,2),
    overtime DECIMAL(10,2) DEFAULT 0,
    deduction DECIMAL(10,2) DEFAULT 0,
    total_amount DECIMAL(10,2),
    status INT DEFAULT 0 COMMENT '0-待核算 1-已核算 2-已发放',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 初始管理员账号 (密码: admin123)
INSERT INTO sys_user (username, password, real_name, role, status) 
VALUES ('admin', '0192023a7bbd73250516f069df18b500', '系统管理员', 1, 1);

-- 测试企业账号 (密码: 123456)
INSERT INTO sys_user (username, password, real_name, role, status) 
VALUES ('enterprise', 'e10adc3949ba59abbe56e057f20f883e', '测试企业', 2, 1);

-- 测试员工账号 (密码: 123456)
INSERT INTO sys_user (username, password, real_name, role, status) 
VALUES ('employee', 'e10adc3949ba59abbe56e057f20f883e', '测试员工', 3, 1);
