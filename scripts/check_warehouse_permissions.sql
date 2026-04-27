-- Check low-code toolbar action source for /sys/warehouse
-- 1) sys_table_operation metadata
SELECT table_code, operation_code, operation_name, permission, position, sort_order, status
FROM sys_table_operation
WHERE table_code = 'sys_warehouse'
ORDER BY sort_order, id;

-- 2) menu permission codes related to warehouse
SELECT menu_id, menu_name, perms, status
FROM sys_menu
WHERE perms LIKE '%warehouse%'
ORDER BY menu_id;

-- 3) which users/roles currently have add/export perms
SELECT u.login_name, r.role_name, m.menu_name, m.perms
FROM sys_user u
JOIN sys_user_role ur ON u.user_id = ur.user_id
JOIN sys_role r ON ur.role_id = r.role_id
JOIN sys_role_menu rm ON r.role_id = rm.role_id
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE m.perms IN (
  'system:warehouse:add',
  'system:warehouse:export',
  'base:warehouse:add',
  'base:warehouse:export'
)
ORDER BY u.login_name, r.role_name, m.perms;
