package com.abtk.product.dao.util;

import com.abtk.product.common.exception.ServiceException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * SQL 注入防御工具类
 * 用于校验动态表名、字段名，防止 SQL 注入攻击
 */
public class SqlInjectionValidator {

    /** 允许访问的表名白名单 */
    private static final Set<String> ALLOWED_TABLES = new HashSet<>(Arrays.asList(
            // 系统基础表
            "sys_user", "sys_role", "sys_menu", "sys_dept", "sys_dict_type", "sys_dict_data",
            "sys_config", "sys_notice", "sys_post", "sys_login_log", "sys_oper_log",
            "sys_role_dept", "sys_role_menu", "sys_user_post", "sys_user_role",
            // 业务表
            "wms_warehouse", "wms_area", "wms_shelf", "wms_inventory",
            "wms_product", "wms_supplier", "wms_customer",
            "wms_stock_in", "wms_stock_out", "wms_transfer",
            "wms_stock_take", "wms_check",
            // 仓库相关表
            "sys_warehouse", "sys_warehouse_receiver"
    ));

    /** 允许访问的字段名白名单 */
    private static final Set<String> ALLOWED_FIELDS = new HashSet<>(Arrays.asList(
            // 通用字段
            "id", "create_time", "create_by", "update_time", "update_by",
            "isdeleted", "del_flag", "status", "remark", "sort", "order_num",
            // 业务字段
            "user_id", "user_name", "nick_name", "email", "phonenumber", "sex",
            "dept_id", "role_id", "role_name", "parent_id", "dept_name",
            "warehouse_id", "warehouse_code", "warehouse_name",
            "area_id", "area_code", "area_name",
            "product_id", "product_code", "product_name",
            "supplier_id", "supplier_name", "customer_id", "customer_name"
    ));

    /** 表名正则：仅允许 字母、数字、下划线 */
    private static final Pattern TABLE_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]{0,63}$");

    /** 字段名正则：仅允许 字母、数字、下划线 */
    private static final Pattern FIELD_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]{0,63}$");

    /**
     * 校验表名是否在白名单中
     */
    public static void validateTable(String tableCode) {
        if (tableCode == null || tableCode.trim().isEmpty()) {
            throw new ServiceException("表名不能为空");
        }
        String trimmed = tableCode.trim();
        if (!ALLOWED_TABLES.contains(trimmed)) {
            throw new ServiceException("表名不在允许范围内: " + trimmed);
        }
    }

    /**
     * 校验字段名是否在白名单中
     */
    public static void validateField(String field) {
        if (field == null || field.trim().isEmpty()) {
            throw new ServiceException("字段名不能为空");
        }
        String trimmed = field.trim();
        String fieldPart = trimmed.contains(".") ? trimmed.substring(trimmed.lastIndexOf(".") + 1) : trimmed;
        if (!ALLOWED_FIELDS.contains(fieldPart)) {
            throw new ServiceException("字段名不在允许范围内: " + fieldPart);
        }
    }

    /**
     * 校验表名格式（正则校验，用于动态表名扩展场景）
     */
    public static void validateTableFormat(String tableCode) {
        if (tableCode == null || tableCode.trim().isEmpty()) {
            throw new ServiceException("表名不能为空");
        }
        String trimmed = tableCode.trim();
        if (!TABLE_PATTERN.matcher(trimmed).matches()) {
            throw new ServiceException("表名格式不合法: " + trimmed);
        }
    }

    /**
     * 校验字段格式
     */
    public static void validateFieldFormat(String field) {
        if (field == null || field.trim().isEmpty()) {
            throw new ServiceException("字段名不能为空");
        }
        String trimmed = field.trim();
        if (!FIELD_PATTERN.matcher(trimmed).matches()) {
            throw new ServiceException("字段名格式不合法: " + trimmed);
        }
    }

    /**
     * 校验表名是否存在于 sys_table_meta 配置表（T0088 审计要求）
     * 此方法用于增强安全验证，确保动态表名来自受信任的配置源
     *
     * @param tableCode 表编码
     * @param tableMetaMapper TableMetaMapper 实例
     * @return 如果表存在则返回 true，否则抛出异常
     */
    public static boolean validateTableFromMeta(String tableCode, com.abtk.product.dao.mapper.TableMetaMapper tableMetaMapper) {
        if (tableCode == null || tableCode.trim().isEmpty()) {
            throw new ServiceException("表名不能为空");
        }
        String trimmed = tableCode.trim();

        // 先检查白名单（第一层防护）
        if (!ALLOWED_TABLES.contains(trimmed)) {
            // 不在白名单中，再检查 sys_table_meta 配置表（第二层防护）
            com.abtk.product.dao.entity.TableMeta tableMeta = tableMetaMapper.selectByTableCode(trimmed);
            if (tableMeta == null) {
                throw new ServiceException("非法表名：表不在白名单且未在 sys_table_meta 中配置: " + trimmed);
            }
        }
        return true;
    }
}
