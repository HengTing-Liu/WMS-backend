-- Backup of menu record before deletion
-- Date: 2026-03-21 23:24:00
-- Reason: Remove test menu "测试一级菜单" as per bug fix request

INSERT INTO sys_menu (
    menu_id, menu_name, parent_id, order_num, path, component, query,
    route_name, is_frame, is_cache, menu_type, visible, status, perms,
    icon, create_by, create_time, update_by, update_time, remark
) VALUES (
    1377, '测试一级菜单', 0, 99, '', '/Layout', NULL,
    '', 1, 0, 'C', '1', '0', NULL,
    'test', '', '2026-03-21 08:55:11', '', NULL, NULL
);

-- Note: This backup can be restored by running this SQL file
-- However, menu_id 1377 may need to be adjusted if AUTO_INCREMENT has changed
