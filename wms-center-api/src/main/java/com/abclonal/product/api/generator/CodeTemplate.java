package com.abclonal.product.api.generator;

import java.io.Serializable;

/**
 * 代码模板
 *
 * @author backend1
 * @date 2025-06-18
 */
public class CodeTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
    private String id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板描述
     */
    private String description;

    /**
     * 模板类型（entity/mapper/service/controller/vue-list/vue-form）
     */
    private String type;

    /**
     * 生成文件后缀
     */
    private String suffix;

    /**
     * 是否选中
     */
    private Boolean selected;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
