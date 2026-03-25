package com.abclonal.product.web.controller.sys;

import com.abclonal.product.common.domain.R;
import com.abclonal.product.common.web.controller.BaseController;
import com.abclonal.product.dao.mapper.DynamicMapper;
import com.abclonal.product.dao.util.SqlInjectionValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 通用数据查询Controller
 * 提供动态API数据查询接口，支持Cascader、Select等组件的API数据源
 */
@Slf4j
@Tag(name = "通用数据查询", description = "动态数据查询接口")
@RestController
@RequestMapping("/api/system/data")
public class DataController extends BaseController {

    @Autowired
    private DynamicMapper dynamicMapper;

    /** SQL关键字黑名单 */
    private static final Pattern SQL_KEYWORDS_PATTERN = Pattern.compile(
            "\\b(INSERT|UPDATE|DELETE|DROP|TRUNCATE|ALTER|CREATE|EXEC|EXECUTE|UNION|SELECT|FROM|WHERE|AND|OR|NOT|NULL|IS|LIKE|IN|BETWEEN|EXISTS|CASE|WHEN|THEN|ELSE|END|DECLARE|CAST|CONVERT|CHAR|VARCHAR|NVARCHAR|NCHAR|TABLE|DATABASE|SCHEMA|GRANT|REVOKE|DENY)\\b",
            Pattern.CASE_INSENSITIVE);

    /** ORDER BY 字段白名单 */
    private static final Pattern ORDER_BY_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*(\\s+(ASC|DESC))?$", Pattern.CASE_INSENSITIVE);

    /**
     * 通用 API 数据查询接口
     * 支持自定义查询，用于Cascader、Select等组件的数据源
     */
    @Operation(summary = "通用数据查询", description = "根据API URL和参数动态查询数据")
    @GetMapping("/query")
    public R<List<Map<String, Object>>> queryData(
            @Parameter(description = "查询表名", required = true)
            @RequestParam String table,
            @Parameter(description = "标签字段", required = true)
            @RequestParam String labelField,
            @Parameter(description = "值字段", required = true)
            @RequestParam String valueField,
            @Parameter(description = "父级字段（级联选择）")
            @RequestParam(required = false) String parentField,
            @Parameter(description = "父级值（级联选择）")
            @RequestParam(required = false) String parentValue,
            @Parameter(description = "查询条件字段名")
            @RequestParam(required = false) String whereField,
            @Parameter(description = "查询条件字段值")
            @RequestParam(required = false) String whereValue,
            @Parameter(description = "排序字段")
            @RequestParam(required = false) String orderBy) {

        try {
            // 1. SQL注入校验 - 表名和字段名
            SqlInjectionValidator.validateTable(table);
            SqlInjectionValidator.validateField(labelField);
            SqlInjectionValidator.validateField(valueField);
            if (parentField != null && !parentField.isEmpty()) {
                SqlInjectionValidator.validateField(parentField);
            }

            // 2. 校验 whereField 如果存在
            if (whereField != null && !whereField.isEmpty()) {
                SqlInjectionValidator.validateField(whereField);
            }

            // 3. 校验 orderBy 格式
            if (orderBy != null && !orderBy.isEmpty()) {
                if (!ORDER_BY_PATTERN.matcher(orderBy.trim()).matches()) {
                    return R.fail("排序字段格式不合法");
                }
                // 额外校验 orderBy 中的字段名
                String orderField = orderBy.trim().split("\\s+")[0];
                SqlInjectionValidator.validateField(orderField);
            }

            // 构建查询SQL
            StringBuilder sql = new StringBuilder("SELECT ");
            sql.append(labelField).append(" as label, ");
            sql.append(valueField).append(" as value ");

            // 如果是级联选择，添加parentField
            if (parentField != null && !parentField.isEmpty()) {
                sql.append(", ").append(parentField).append(" as parent_value ");
            }

            sql.append(" FROM ").append(table);

            // 构建WHERE条件 - 使用具名参数替代字符串拼接
            List<String> conditions = new java.util.ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            // 使用参数化查询替代直接的 where 字符串拼接
            if (whereField != null && !whereField.isEmpty() && whereValue != null && !whereValue.isEmpty()) {
                conditions.add(whereField + " = #{whereValue}");
                params.put("whereValue", whereValue);
            }

            if (parentField != null && !parentField.isEmpty() && parentValue != null && !parentValue.isEmpty()) {
                conditions.add(parentField + " = #{parentValue}");
                params.put("parentValue", parentValue);
            }

            if (!conditions.isEmpty()) {
                sql.append(" WHERE ").append(String.join(" AND ", conditions));
            }

            if (orderBy != null && !orderBy.isEmpty()) {
                sql.append(" ORDER BY ").append(orderBy);
            }

            log.info("执行动态查询: SQL={}, params={}", sql, params);

            List<Map<String, Object>> result = dynamicMapper.executeQuery(sql.toString(), params);
            return R.ok(result);

        } catch (Exception e) {
            log.error("通用数据查询失败: table={}", table, e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }
}
