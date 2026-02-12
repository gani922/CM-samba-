-- ============================================================
-- DATA.SQL - With Your COUNTRY Data and Proper Relationships
-- ============================================================

-- Clear existing data in correct order
DELETE FROM user_address;
DELETE FROM COMPANY_ADDRESS;
DELETE FROM sap_info;
DELETE FROM COMPANY_INFO;
DELETE FROM PORTAL_USER_ROLE;
DELETE FROM ROLE_RESOURCE;
DELETE FROM RESOURCE;
DELETE FROM ROLE;
DELETE FROM COUNTRY;
DELETE FROM jwt_blacklist;
DELETE FROM sale_organize;
DELETE FROM bu_info;
DELETE FROM admin_login_info;

-- ============================================================
-- 1. ADMINISTRATION MODULE
-- ============================================================
INSERT INTO admin_login_info (admin_id, username, password_hash, email, full_name, role, phone_number, department, is_active) VALUES
('ADM001', 'sambasiva', '$2a$12$vXZRN8Fs3sqKYR8VDqKN9Ofm70ciFMT7rPw3JPHbIZs4py6LRE2QW', 'reachme318151@gmail.com', 'Super Administrator', 'SUPER_ADMIN', '+1-555-0101', 'IT', TRUE),
('ADM002', 'kalyan', '$2a$12$vXZRN8Fs3sqKYR8VDqKN9Ofm70ciFMT7rPw3JPHbIZs4py6LRE2QW', 'kalyanvenkat6942@company.com', 'Business Unit Admin', 'BU_ADMIN', '+1-555-0102', 'Operations', TRUE),
('ADM003', 'vivekLala', '$2a$12$vXZRN8Fs3sqKYR8VDqKN9Ofm70ciFMT7rPw3JPHbIZs4py6LRE2QW', 'countryadmin@company.com', 'Country Administrator', 'COUNTRY_ADMIN', '+1-555-0103', 'Management', TRUE);

-- ============================================================
-- 2. BUSINESS UNIT MODULE - With IDs matching your COUNTRY data
-- ============================================================
-- First, let's create BUs with specific IDs that match your COUNTRY data
-- We need BU with ID 5 for your countries (since BU_ID=5 in your COUNTRY data)

-- Insert BUs with specific IDs (H2 allows this with identity column)
-- Let's create a BU with ID 5 first
INSERT INTO bu_info (id, name, friendly_name, layer, admin_id, created_by, creation_time) VALUES
(5, 'APAC Operations', 'APAC Ops', 1, 'ADM001', 'ADM001', CURRENT_TIMESTAMP);

-- Insert other BUs
--INSERT INTO bu_info (id ,name, friendly_name, layer, admin_id, created_by, creation_time) VALUES
--(5,'EMEA', 'Europe Middle East Africa', 1, 'ADM001', 'ADM001', CURRENT_TIMESTAMP),
--(5,'NA', 'North America', 1, 'ADM001', 'ADM001', CURRENT_TIMESTAMP);

-- Now insert sub-BUs with specific IDs that match your SUB_BU_ID values
INSERT INTO bu_info (id, name, friendly_name, layer, admin_id, parent_id, created_by, creation_time) VALUES
(443, 'India Sub-BU', 'India Operations', 2, 'ADM002', 5, 'ADM002', CURRENT_TIMESTAMP),   -- SUB_BU_ID=443
(661, 'Bangladesh Sub-BU', 'Bangladesh Operations', 2, 'ADM002', 5, 'ADM002', CURRENT_TIMESTAMP), -- SUB_BU_ID=661
(10026, 'Nepal Sub-BU', 'Nepal Operations', 2, 'ADM002', 5, 'ADM002', CURRENT_TIMESTAMP), -- SUB_BU_ID=10026
(662, 'Sri-lanka Sub-BU', 'Sri-lanka Operations', 2, 'ADM002', 5, 'ADM002', CURRENT_TIMESTAMP); -- SUB_BU_ID=662

-- ============================================================
-- 3. COUNTRY MODULE - Your Original Data with Proper Relationships
-- ============================================================
-- Now your COUNTRY data has proper relationships:
-- BU_ID=5 points to bu_info.id=5 (APAC Operations)
-- SUB_BU_ID points to specific sub-BUs
INSERT INTO COUNTRY (ID, COUNTRY_NAME, BU_ID, SUB_BU_ID) VALUES
(444, 'India', 5, 443),      -- Country ID 444 → BU 5 → Sub-BU 443 (India Operations)
(662, 'Bangladesh', 5, 661), -- Country ID 662 → BU 5 → Sub-BU 661 (Bangladesh Operations)
(10027, 'Nepal', 5, 10026),  -- Country ID 10027 → BU 5 → Sub-BU 10026 (Nepal Operations)
(663, 'Sri-lanka', 5, 662);  -- Country ID 663 → BU 5 → Sub-BU 662 (Sri-lanka Operations)

-- ============================================================
-- 4. SALES ORGANIZATION MODULE
-- ============================================================
INSERT INTO sale_organize (sale_organize_id, sale_organize_short, sale_organize_name, channel, admin_id, created_by) VALUES
('8304', 'IN', 'India Branch', 'B2B', 'ADM001', 'ADM001'),
('8305', 'BD', 'Bangladesh Branch', 'B2B', 'ADM001', 'ADM001'),
('8306', 'NP', 'Nepal Branch', 'B2C', 'ADM002', 'ADM001'),
('8307', 'LK', 'Sri Lanka Branch', 'B2B', 'ADM002', 'ADM001');

-- ============================================================
-- 5. COMPANY MODULE - Companies in Your Countries
-- ============================================================
-- Companies are assigned to specific countries from your COUNTRY table
INSERT INTO COMPANY_INFO (ID, BU_ID, COUNTRY_ID, COMPANY_NAME, DELETE_STATUS, IS_ACTIVE, SAP_ID, EMAIL, PHONE, ADDRESS, CITY, STATE_NOTE, TAX_ID, COMPANY_TYPE) VALUES
-- India (Country ID 444)
(1001, 5, 444, 'Tech Solutions India Pvt Ltd', 'ACTIVE', 'YES', 'SAP001', 'info@techsolutions.in', '+91-80-23456789', '123 MG Road, Bangalore', 'Bangalore', 'Karnataka', 'GSTIN29AAAAA1000A1Z5', 'PRIVATE'),
(1002, 5, 444, 'Global Manufacturing India', 'ACTIVE', 'YES', 'SAP002', 'contact@globalmfg.in', '+91-22-98765432', '456 Andheri East, Mumbai', 'Mumbai', 'Maharashtra', 'GSTIN27BBBBB2000B2Z6', 'PUBLIC'),
-- Bangladesh (Country ID 662)
(1003, 5, 662, 'Dhaka Textiles Ltd', 'ACTIVE', 'YES', 'SAP003', 'sales@dhakatextiles.bd', '+880-2-9876543', '789 Motijheel, Dhaka', 'Dhaka', 'Dhaka Division', 'BGD123456789', 'PRIVATE'),
-- Nepal (Country ID 10027)
(1004, 5, 10027, 'Kathmandu Electronics', 'ACTIVE', 'YES', 'SAP004', 'info@kathmanduelec.np', '+977-1-8765432', '101 Thamel, Kathmandu', 'Kathmandu', 'Bagmati', 'NPL123456789', 'PRIVATE'),
-- Sri Lanka (Country ID 663)
(1005, 5, 663, 'Colombo Trading Co', 'ACTIVE', 'YES', 'SAP005', 'contact@colombotrading.lk', '+94-11-7654321', '456 Galle Road, Colombo', 'Colombo', 'Western', 'LKA123456789', 'PUBLIC');

-- ============================================================
-- 6. USER MODULE - SAP Users for Each Company
-- ============================================================
INSERT INTO sap_info (
    user_id, user_sap_id, is_lock, user_name,
    user_email, user_mobile, user_mobile_country_code,
    user_level, sap_user_group, user_country_code,
    delete_status, created_by, user_grade, region, region_id,
    pan_number, user_sale_name, sale_channel,
    start_valid_date, end_valid_date, sale_organize_id
) VALUES
-- India - SAP001
('USR001', 'SAP001', 'N', 'Rajesh Kumar',
 'rajesh.kumar@techsolutions.in', '9876543210', '+91',
 'LEVEL1', 'GROUP_A', 'IN', '0', 'ADM001',
 'GRADE_A', 'SOUTH', 'REG001', 'ABCDE1234F', 'Rajesh Kumar',
 'B2B', CURRENT_TIMESTAMP, DATEADD('YEAR', 1, CURRENT_TIMESTAMP), '8304'),

-- India - SAP002
('USR002', 'SAP002', 'N', 'Priya Sharma',
 'priya.sharma@globalmfg.in', '9876543211', '+91',
 'LEVEL2', 'GROUP_B', 'IN', '0', 'ADM001',
 'GRADE_B', 'WEST', 'REG002', 'FGHIJ5678K', 'Priya Sharma',
 'B2C', CURRENT_TIMESTAMP, DATEADD('YEAR', 1, CURRENT_TIMESTAMP), '8304'),

-- Bangladesh - SAP003
('USR003', 'SAP003', 'N', 'Mohammad Ali',
 'mohammad.ali@dhakatextiles.bd', '8801712345678', '+880',
 'LEVEL1', 'GROUP_A', 'BD', '0', 'ADM001',
 'GRADE_A', 'CENTRAL', 'REG101', NULL, 'Mohammad Ali',
 'B2B', CURRENT_TIMESTAMP, DATEADD('YEAR', 1, CURRENT_TIMESTAMP), '8305'),

-- Nepal - SAP004
('USR004', 'SAP004', 'Y', 'Bikash Thapa',
 'bikash.thapa@kathmanduelec.np', '977981234567', '+977',
 'LEVEL3', 'GROUP_C', 'NP', '0', 'ADM001',
 'GRADE_C', 'BAGMATI', 'REG201', NULL, 'Bikash Thapa',
 'B2B', CURRENT_TIMESTAMP, DATEADD('YEAR', 1, CURRENT_TIMESTAMP), '8306'),

-- Sri Lanka - SAP005
('USR005', 'SAP005', 'N', 'Chaminda Perera',
 'chaminda@colombotrading.lk', '94771234567', '+94',
 'LEVEL2', 'GROUP_B', 'LK', '0', 'ADM001',
 'GRADE_B', 'WESTERN', 'REG301', NULL, 'Chaminda Perera',
 'B2B', CURRENT_TIMESTAMP, DATEADD('YEAR', 1, CURRENT_TIMESTAMP), '8307');

-- ============================================================
-- 7. ADDRESS MODULE
-- ============================================================
-- Company Addresses
INSERT INTO COMPANY_ADDRESS (ADDRESS_ID, USER_SAP_ID, ADDRESS_COUNTRY_CODE, ADDRESS_PROVINCE, ADDRESS_CITY, ADDRESS_STREET, ADDRESS_ZIPCODE, ADDRESS_TYPE, DEFAULT_ADDRESS, ADDRESS, DELETE_STATUS) VALUES
(1001, 'SAP001', 'IN', 'Karnataka', 'Bangalore', '123 MG Road', '560001', 'BILLING', 'YES', '123 MG Road, Bangalore, India', 'ACTIVE'),
(1002, 'SAP002', 'IN', 'Maharashtra', 'Mumbai', '456 Andheri East', '400069', 'BILLING', 'YES', '456 Andheri East, Mumbai, India', 'ACTIVE'),
(1003, 'SAP003', 'BD', 'Dhaka Division', 'Dhaka', '789 Motijheel', '1000', 'BILLING', 'YES', '789 Motijheel, Dhaka, Bangladesh', 'ACTIVE'),
(1004, 'SAP004', 'NP', 'Bagmati', 'Kathmandu', '101 Thamel', '44600', 'BILLING', 'YES', '101 Thamel, Kathmandu, Nepal', 'ACTIVE'),
(1005, 'SAP005', 'LK', 'Western', 'Colombo', '456 Galle Road', '00100', 'BILLING', 'YES', '456 Galle Road, Colombo, Sri Lanka', 'ACTIVE');

-- User Personal Addresses
INSERT INTO user_address (address_id, user_sap_id, address_type, address_line1, address_line2, city, state, postal_code, country, is_default, delete_status) VALUES
('ADD001', 'SAP001', 'HOME', '789 Koramangala', '3rd Block', 'Bangalore', 'Karnataka', '560034', 'India', 'Y', '0'),
('ADD002', 'SAP002', 'HOME', '101 Bandra West', 'Sea View Apartments', 'Mumbai', 'Maharashtra', '400050', 'India', 'Y', '0'),
('ADD003', 'SAP003', 'HOME', '234 Gulshan', 'Road 45', 'Dhaka', 'Dhaka Division', '1212', 'Bangladesh', 'Y', '0'),
('ADD004', 'SAP004', 'HOME', '567 Boudha', 'Circle Road', 'Kathmandu', 'Bagmati', '44600', 'Nepal', 'Y', '0'),
('ADD005', 'SAP005', 'HOME', '890 Kollupitiya', 'Lane 5', 'Colombo', 'Western', '00300', 'Sri Lanka', 'Y', '0');

-- ============================================================
-- 8. RBAC MODULE
-- ============================================================
-- Resources
INSERT INTO RESOURCE (NAME, TYPE, AUTHORITY_ATTR, URL, PARENT_ID, CODE, SORT) VALUES
('Dashboard', 'M', 'VIEW_DASHBOARD', '/dashboard', 0, 'DASHBOARD', 1),
('User Management', 'M', 'VIEW_USER_MGMT', '/users', 0, 'USER_MGMT', 2),
('Company Management', 'M', 'VIEW_COMPANY_MGMT', '/companies', 0, 'COMPANY_MGMT', 3),
('Reports', 'M', 'VIEW_REPORTS', '/reports', 0, 'REPORTS', 4),
('View Users', 'P', 'USER_VIEW', '/users/view', 2, 'USER_VIEW', 1),
('Create Users', 'P', 'USER_CREATE', '/users/create', 2, 'USER_CREATE', 2);

-- Roles (BU_ID should be 5 to match your structure)
INSERT INTO ROLE (BU_ID, ROLE_NAME, DELETE_STATUS, IS_DEFAULT, CREATED_BY, TYPE, DESCRIPTION) VALUES
(5, 'APAC Super Admin', '0', '1', 'ADM001', 1, 'Full access for APAC Operations'),
(5, 'APAC BU Admin', '0', '1', 'ADM001', 2, 'Business Unit admin for APAC'),
(5, 'Country Manager', '0', '1', 'ADM001', 3, 'Country level manager for APAC countries');

-- Role-Resource Mapping
INSERT INTO ROLE_RESOURCE (ROLE_ID, RESOURCE_ID) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),  -- APAC Super Admin
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5),          -- APAC BU Admin
(3, 1), (3, 3), (3, 5);                           -- Country Manager

-- User-Role Assignment
INSERT INTO PORTAL_USER_ROLE (USER_ID, ROLE_ID) VALUES
('ADM001', 1),  -- Super Admin for APAC
('ADM002', 2),  -- BU Admin for APAC
('ADM003', 3);  -- Country Manager

-- ============================================================
-- 9. SECURITY MODULE
-- ============================================================
INSERT INTO jwt_blacklist (token) VALUES
('eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.expired.token.123');

-- ============================================================
-- VERIFICATION QUERIES - Showing Complete Relationships
-- ============================================================
SELECT '====== COMPLETE RELATIONSHIP CHAIN ======' as Verification;

-- 1. Business Unit → Country → Company → User Chain
SELECT
    b.name as Business_Unit,
    b.id as BU_ID,
    c.COUNTRY_NAME,
    c.ID as Country_ID,
    c.SUB_BU_ID,
    ci.COMPANY_NAME,
    ci.SAP_ID,
    s.user_name as SAP_User,
    s.user_email,
    so.sale_organize_name as Sales_Branch
FROM bu_info b
JOIN COUNTRY c ON b.id = c.BU_ID
JOIN COMPANY_INFO ci ON c.ID = ci.COUNTRY_ID
JOIN sap_info s ON ci.SAP_ID = s.user_sap_id
LEFT JOIN sale_organize so ON s.sale_organize_id = so.sale_organize_id
WHERE b.id = 5  -- Your APAC Operations BU
ORDER BY c.COUNTRY_NAME, ci.COMPANY_NAME;

-- 2. Country → Sub-BU Relationship
SELECT
    c.COUNTRY_NAME,
    c.ID as Country_ID,
    b_main.name as Main_Business_Unit,
    b_main.id as Main_BU_ID,
    b_sub.name as Sub_Business_Unit,
    b_sub.id as Sub_BU_ID,
    b_sub.parent_id
FROM COUNTRY c
JOIN bu_info b_main ON c.BU_ID = b_main.id
JOIN bu_info b_sub ON c.SUB_BU_ID = b_sub.id
ORDER BY c.COUNTRY_NAME;

-- 3. Company with Address Details
SELECT
    ci.COMPANY_NAME,
    ci.COUNTRY_ID,
    c.COUNTRY_NAME,
    ci.SAP_ID,
    ca.ADDRESS,
    ca.ADDRESS_CITY,
    ca.ADDRESS_PROVINCE,
    ua.address_line1 as User_Address,
    ua.city as User_City
FROM COMPANY_INFO ci
JOIN COUNTRY c ON ci.COUNTRY_ID = c.ID
LEFT JOIN COMPANY_ADDRESS ca ON ci.SAP_ID = ca.USER_SAP_ID AND ca.DEFAULT_ADDRESS = 'YES'
LEFT JOIN user_address ua ON ci.SAP_ID = ua.user_sap_id AND ua.is_default = 'Y'
WHERE ci.BU_ID = 5
ORDER BY c.COUNTRY_NAME;

-- 4. Admin Permissions Flow
SELECT
    a.username as Admin,
    a.role as Admin_Role,
    r.ROLE_NAME as Assigned_Role,
    b.name as Business_Unit,
    COUNT(re.ID) as Permission_Count,
    GROUP_CONCAT(re.NAME) as Permissions
FROM admin_login_info a
JOIN PORTAL_USER_ROLE pur ON a.admin_id = pur.USER_ID
JOIN ROLE r ON pur.ROLE_ID = r.ID
JOIN bu_info b ON r.BU_ID = b.id
JOIN ROLE_RESOURCE rr ON r.ID = rr.ROLE_ID
JOIN RESOURCE re ON rr.RESOURCE_ID = re.ID
GROUP BY a.username, a.role, r.ROLE_NAME, b.name;

-- Summary
SELECT '====== DATA SUMMARY ======' as Summary;
SELECT 'Total Countries: ' || COUNT(*) || ' (Your original data preserved)' FROM COUNTRY;
SELECT 'Business Units: ' || COUNT(*) || ' (Including your SUB_BU_ID references)' FROM bu_info;
SELECT 'Companies in APAC: ' || COUNT(*) FROM COMPANY_INFO WHERE BU_ID = 5;
SELECT 'SAP Users: ' || COUNT(*) FROM sap_info;
SELECT 'Company Addresses: ' || COUNT(*) FROM COMPANY_ADDRESS;
SELECT 'User Addresses: ' || COUNT(*) FROM user_address;
SELECT 'Sales Organizations: ' || COUNT(*) FROM sale_organize;

-- Show the complete hierarchy
SELECT '====== COMPLETE HIERARCHY ======' as Hierarchy;
SELECT
    'BU: ' || b_main.name || ' (ID: ' || b_main.id || ')' as Level1,
    '→ Sub-BU: ' || b_sub.name || ' (ID: ' || b_sub.id || ')' as Level2,
    '→ Country: ' || c.COUNTRY_NAME || ' (ID: ' || c.ID || ')' as Level3,
    '→ Companies: ' || COUNT(ci.ID) as Company_Count
FROM bu_info b_main
LEFT JOIN bu_info b_sub ON b_sub.parent_id = b_main.id
LEFT JOIN COUNTRY c ON c.SUB_BU_ID = b_sub.id
LEFT JOIN COMPANY_INFO ci ON ci.COUNTRY_ID = c.ID
WHERE b_main.id = 5
GROUP BY b_main.name, b_main.id, b_sub.name, b_sub.id, c.COUNTRY_NAME, c.ID
ORDER BY b_sub.name, c.COUNTRY_NAME;