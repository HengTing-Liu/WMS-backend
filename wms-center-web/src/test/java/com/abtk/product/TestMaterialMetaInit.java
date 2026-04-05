package com.abtk.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 物料元数据初始化测试
 * 执行完成后请删除此类
 */
@SpringBootTest(classes = com.abtk.product.web.Application.class)
@ActiveProfiles("dev")
public class TestMaterialMetaInit {

    private static final Logger log = LoggerFactory.getLogger(TestMaterialMetaInit.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void initMaterialMeta() {
        log.info("开始插入物料表级元数据...");
        // 1. 表级元数据
        jdbcTemplate.execute(
            "INSERT INTO sys_table_meta (table_code, table_name, module, entity_class, service_class, permission_code, page_size, is_tree, status, remark, is_deleted_column, create_by, create_time) " +
            "VALUES ('sys_material', '物料档案', 'sys', 'com.abtk.product.dao.entity.Material', 'com.abtk.product.service.sys.service.MaterialService', 'sys:material', 20, 0, 1, '物料基础信息管理', 'del_flag', 'system', NOW()) " +
            "ON DUPLICATE KEY UPDATE table_name = VALUES(table_name), entity_class = VALUES(entity_class), service_class = VALUES(service_class), permission_code = VALUES(permission_code), update_time = NOW()"
        );
        log.info("表级元数据插入完成");

        // 2. 字段元数据（field 使用 camelCase, column_name 使用 snake_case）
        String[][] fields = {
            {"id",            "主键ID",       "bigint",  "number",  "0", "0", "0", "0", "0", "NULL", "0",  "0"},
            {"materialCode",  "物料编码",     "string",  "input",   "1", "1", "1", "1", "1", "140",  "1",  "1"},
            {"materialName", "物料名称",     "string",  "input",   "1", "1", "1", "1", "1", "180",  "2",  "1"},
            {"specification", "规格型号",    "string",  "input",   "1", "1", "1", "0", "0", "160",  "3",  "1"},
            {"unit",          "单位",        "string",  "input",   "1", "1", "1", "0", "0", "80",   "4",  "1"},
            {"category",     "分类",        "string",  "input",   "1", "1", "1", "0", "0", "120",  "5",  "1"},
            {"status",        "状态",        "int",     "switch",  "1", "1", "1", "0", "1", "80",   "6",  "1"},
            {"delFlag",       "是否删除",    "int",     "number",  "0", "0", "0", "0", "0", "NULL", "7",  "0"},
            {"createBy",      "创建人",      "string",  "input",   "0", "0", "0", "0", "0", "100",  "8",  "1"},
            {"createTime",    "创建时间",    "datetime","datetime","1", "0", "1", "1", "0", "170",  "9",  "1"},
            {"updateBy",      "更新人",      "string",  "input",   "0", "0", "0", "0", "0", "100",  "10", "1"},
            {"updateTime",    "更新时间",    "datetime","datetime","0", "0", "0", "1", "0", "170",  "11", "1"},
        };

        // column_name 与数据库字段名对应（spec 对应数据库列 spec，不是 specification）
        String[] columnNames = {
            "id", "material_code", "material_name", "spec", "unit", "category",
            "status", "del_flag", "create_by", "create_time", "update_by", "update_time"
        };

        for (int i = 0; i < fields.length; i++) {
            String[] f = fields[i];
            String colName = columnNames[i];
            jdbcTemplate.update(
                "INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, show_in_list, show_in_form, searchable, sortable, required, width, sort_order, status, create_by, create_time, column_name) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'system', NOW(), ?) " +
                "ON DUPLICATE KEY UPDATE title = VALUES(title), data_type = VALUES(data_type), form_type = VALUES(form_type), show_in_list = VALUES(show_in_list), show_in_form = VALUES(show_in_form), searchable = VALUES(searchable), sortable = VALUES(sortable), required = VALUES(required), width = VALUES(width), sort_order = VALUES(sort_order), column_name = VALUES(column_name), update_time = NOW()",
                f[0], f[1], f[2], f[3],
                Integer.parseInt(f[4]), Integer.parseInt(f[5]), Integer.parseInt(f[6]),
                Integer.parseInt(f[7]), Integer.parseInt(f[8]),
                "NULL".equals(f[9]) ? null : Integer.parseInt(f[9]),
                Integer.parseInt(f[10]), Integer.parseInt(f[11]),
                colName
            );
            log.info("插入字段: {} -> {}", f[0], colName);
        }
        log.info("字段元数据插入完成，共 {} 条", fields.length);

        // 3. 验证
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
            "SELECT field, column_name, title, searchable, show_in_list FROM sys_column_meta WHERE table_code='sys_material' ORDER BY sort_order"
        );
        log.info("验证查询结果 ({} 条):", result.size());
        for (Map<String, Object> row : result) {
            log.info("  field={}, column_name={}, title={}, searchable={}, show_in_list={}",
                row.get("field"), row.get("column_name"), row.get("title"), row.get("searchable"), row.get("show_in_list"));
        }
    }

    @Test
    public void verifyMaterialMeta() {
        List<Map<String, Object>> tables = jdbcTemplate.queryForList(
            "SELECT * FROM sys_table_meta WHERE table_code = 'sys_material'"
        );
        log.info("sys_table_meta 查询结果数量: {}", tables.size());

        List<Map<String, Object>> columns = jdbcTemplate.queryForList(
            "SELECT field, title, searchable, show_in_list FROM sys_column_meta WHERE table_code='sys_material' ORDER BY sort_order"
        );
        log.info("sys_column_meta 字段数量: {}", columns.size());
        for (Map<String, Object> col : columns) {
            log.info("  {} - {} (searchable={}, show_in_list={})",
                col.get("field"), col.get("title"), col.get("searchable"), col.get("show_in_list"));
        }
    }
}