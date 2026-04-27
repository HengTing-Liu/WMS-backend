-- 重置所有默认用户密码为 admin123
-- 使用 BCrypt 加密后的密码: admin123 = $2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2

UPDATE sys_user SET 
    password = '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2',
    status = '0',
    del_flag = '0'
WHERE login_name IN ('admin', 'warehouse', 'inbound', 'outbound', 'inventory');

-- 验证更新结果
SELECT user_id, login_name, nick_name, status, del_flag, 
       CASE WHEN password = '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2' THEN '密码正确' ELSE '密码不匹配' END as pwd_check
FROM sys_user 
WHERE login_name IN ('admin', 'warehouse', 'inbound', 'outbound', 'inventory');
