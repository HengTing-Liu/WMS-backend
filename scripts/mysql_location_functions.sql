-- =============================================
-- 库位管理 MySQL 函数
-- =============================================

-- 删除已存在的函数
DROP FUNCTION IF EXISTS queryChildrenIds;

-- 创建递归查询子节点ID的函数
DELIMITER //

CREATE FUNCTION queryChildrenIds(rootId BIGINT)
RETURNS VARCHAR(4000)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE ids VARCHAR(4000);
    DECLARE tempIds VARCHAR(4000);
    
    SET ids = CAST(rootId AS CHAR);
    SET tempIds = CAST(rootId AS CHAR);
    
    WHILE tempIds IS NOT NULL AND LENGTH(tempIds) > 0 DO
        SELECT GROUP_CONCAT(id) INTO tempIds
        FROM inv_location
        WHERE FIND_IN_SET(parent_id, tempIds) AND is_deleted = 0;
        
        IF tempIds IS NOT NULL THEN
            SET ids = CONCAT(ids, ',', tempIds);
        END IF;
    END WHILE;
    
    RETURN ids;
END //

DELIMITER ;

-- =============================================
-- 添加国际化消息（如果存在sys_message表）
-- =============================================

-- 库位管理相关消息
INSERT IGNORE INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES 
(1, '库位不存在', 'location.not.found', 'i18n_message', '', '', 'N', '0', 'system', NOW(), 'Location not found'),
(2, '上级库位不存在', 'location.parent.not.found', 'i18n_message', '', '', 'N', '0', 'system', NOW(), 'Parent location not found'),
(3, '库位ID不能为空', 'location.id.required', 'i18n_message', '', '', 'N', '0', 'system', NOW(), 'Location ID is required'),
(4, '该库位存在子节点，无法删除', 'location.has.children', 'i18n_message', '', '', 'N', '0', 'system', NOW(), 'Location has children'),
(5, '批量创建成功，共创建{0}条记录', 'batch.create.success', 'i18n_message', '', '', 'N', '0', 'system', NOW(), 'Batch create success');
