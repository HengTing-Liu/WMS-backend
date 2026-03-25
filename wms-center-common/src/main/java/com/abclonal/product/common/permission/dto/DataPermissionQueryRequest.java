package com.abclonal.product.common.permission.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据权限查询请求基类
 * 用于CrudService的请求参数，自动支持数据权限
 * 
 * @author backend2
 */
public class DataPermissionQueryRequest {
    
    /**
     * 页码
     */
    private Integer page = 1;
    
    /**
     * 每页大小
     */
    private Integer pageSize = 20;
    
    /**
     * 查询参数
     */
    private Map<String, Object> params = new HashMap<>();
    
    /**
     * 排序字段
     */
    private String orderBy;
    
    /**
     * 排序方式（asc/desc）
     */
    private String order;
    
    /**
     * 表编码（用于获取权限配置）
     */
    private String tableCode;
    
    // ==================== Getter/Setter ====================
    
    public Integer getPage() {
        return page;
    }
    
    public void setPage(Integer page) {
        this.page = page;
    }
    
    public Integer getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    public Map<String, Object> getParams() {
        return params;
    }
    
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    
    public String getOrderBy() {
        return orderBy;
    }
    
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public String getOrder() {
        return order;
    }
    
    public void setOrder(String order) {
        this.order = order;
    }
    
    public String getTableCode() {
        return tableCode;
    }
    
    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }
    
    // ==================== 便捷方法 ====================
    
    /**
     * 添加查询参数
     */
    public void addParam(String key, Object value) {
        this.params.put(key, value);
    }
    
    /**
     * 获取查询参数
     */
    public Object getParam(String key) {
        return this.params.get(key);
    }
    
    /**
     * 计算偏移量
     */
    public int getOffset() {
        return (page - 1) * pageSize;
    }
}
