package com.abclonal.product.api.generator;

import java.io.Serializable;
import java.util.List;

/**
 * 代码生成配置
 *
 * @author backend1
 * @date 2025-06-18
 */
public class CodeGenerateConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 作者名
     */
    private String author;

    /**
     * 包路径
     */
    private String packageName;

    /**
     * 模块名
     */
    private String moduleName;

    /**
     * 前端路径
     */
    private String frontendPath;

    /**
     * 后端路径
     */
    private String backendPath;

    /**
     * 表ID
     */
    private Long tableId;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 类名
     */
    private String className;

    /**
     * 类描述
     */
    private String classComment;

    /**
     * 选中的模板列表
     */
    private List<String> templates;

    // Getters and Setters
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getFrontendPath() {
        return frontendPath;
    }

    public void setFrontendPath(String frontendPath) {
        this.frontendPath = frontendPath;
    }

    public String getBackendPath() {
        return backendPath;
    }

    public void setBackendPath(String backendPath) {
        this.backendPath = backendPath;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassComment() {
        return classComment;
    }

    public void setClassComment(String classComment) {
        this.classComment = classComment;
    }

    public List<String> getTemplates() {
        return templates;
    }

    public void setTemplates(List<String> templates) {
        this.templates = templates;
    }
}
