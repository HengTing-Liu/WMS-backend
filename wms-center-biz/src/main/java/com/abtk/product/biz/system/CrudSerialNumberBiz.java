package com.abtk.product.biz.system;

import com.abtk.product.dao.entity.ColumnMeta;
import com.abtk.product.dao.mapper.ColumnMetaMapper;
import com.abtk.product.service.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 低代码 CRUD 流水号自动填充业务层
 * <p>供 Controller / Biz 在调用 Service.create 之前预处理数据</p>
 */
@Slf4j
@Component
public class CrudSerialNumberBiz {

    @Autowired
    private ColumnMetaMapper columnMetaMapper;

    @Autowired
    private SysSerialNumberBiz sysSerialNumberBiz;

    /**
     * 为配置了流水号规则且值为空的字段自动生成流水号（Map 数据，用于低代码通用 CRUD）
     */
    public void fillSerialNumbers(String tableCode, Map<String, Object> data) {
        try {
            List<ColumnMeta> columns = columnMetaMapper.selectByTableCode(tableCode);
            if (columns == null || columns.isEmpty()) {
                return;
            }
            String username = SecurityUtils.getUsername();
            for (ColumnMeta col : columns) {
                String serialRule = col.getSerialNumberRule();
                if (serialRule == null || serialRule.isEmpty()) {
                    continue;
                }
                // 数据库中 field 是 snake_case，但前端提交的 data key 可能是 camelCase 或 snake_case
                String snakeField = col.getField();
                String camelField = toCamelCase(snakeField);
                Object currentValue = data.get(camelField);
                if (currentValue == null || "".equals(currentValue)) {
                    currentValue = data.get(snakeField);
                }
                if (currentValue == null || "".equals(currentValue)) {
                    try {
                        String serialNo = sysSerialNumberBiz.generateSerialNumber(serialRule, username);
                        // 优先写回前端使用的 camelCase key，如果不存在则写 snake_case
                        if (data.containsKey(camelField) || !data.containsKey(snakeField)) {
                            data.put(camelField, serialNo);
                        } else {
                            data.put(snakeField, serialNo);
                        }
                    } catch (Exception e) {
                        log.warn("自动生成流水号失败, tableCode={}, field={}, ruleName={}, err={}",
                                tableCode, col.getField(), serialRule, e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.warn("流水号自动填充逻辑异常, tableCode={}, err={}", tableCode, e.getMessage());
        }
    }

    /**
     * 为配置了流水号规则且值为空的字段自动生成流水号（实体对象，用于固定页面 Biz）
     *
     * @param tableCode 表标识（对应 sys_table_meta.table_code）
     * @param entity    实体对象（会被直接修改字段值）
     */
    public void fillForEntity(String tableCode, Object entity) {
        if (entity == null) {
            return;
        }
        try {
            List<ColumnMeta> columns = columnMetaMapper.selectByTableCode(tableCode);
            if (columns == null || columns.isEmpty()) {
                return;
            }
            String username = SecurityUtils.getUsername();
            Class<?> clazz = entity.getClass();
            for (ColumnMeta col : columns) {
                String serialRule = col.getSerialNumberRule();
                if (serialRule == null || serialRule.isEmpty()) {
                    continue;
                }
                // 数据库中 field 是 snake_case，实体类字段可能是 camelCase 或 snake_case
                String snakeField = col.getField();
                String camelField = toCamelCase(snakeField);
                // 新增时强制重新生成流水号并覆盖前端传入的预览值，确保 currentSeq 正确递增
                try {
                    String serialNo = sysSerialNumberBiz.generateSerialNumber(serialRule, username);
                    // 优先设置 camelCase 字段，失败则尝试 snake_case
                    if (!setFieldValue(clazz, entity, camelField, serialNo)) {
                        setFieldValue(clazz, entity, snakeField, serialNo);
                    }
                } catch (Exception e) {
                    log.warn("自动生成流水号失败, tableCode={}, field={}, ruleName={}, err={}",
                            tableCode, col.getField(), serialRule, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.warn("流水号自动填充逻辑异常, tableCode={}, err={}", tableCode, e.getMessage());
        }
    }

    private Object getFieldValue(Class<?> clazz, Object entity, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(entity);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean setFieldValue(Class<?> clazz, Object entity, String fieldName, String value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 下划线转驼峰（如 warehouse_code → warehouseCode）
     */
    private String toCamelCase(String snakeCase) {
        if (snakeCase == null || snakeCase.isEmpty()) {
            return snakeCase;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        for (int i = 0; i < snakeCase.length(); i++) {
            char c = snakeCase.charAt(i);
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(c);
                }
            }
        }
        return result.toString();
    }

    /**
     * 驼峰转下划线（如 warehouseCode → warehouse_code）
     */
    private String toSnakeCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
