package com.abclonal.product.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * 菜单低代码配置映射实体
 * sys_menu_meta_map
 *
 * @author ruoyi
 */
public class SysMenuMetaMap extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 关联 sys_menu.menu_id */
    private Long menuId;

    /** 对应的低代码实体名（如 wms_user） */
    private String entityName;

    /** 渲染模式: tree(树形) | flat(扁平) */
    private String listMode;

    /** 树形父节点字段 */
    private String treeParentField;

    /** 树形显示字段 */
    private String treeLabelField;

    /** 节点类型映射: C=category,M=menu,F=button */
    private String treeNodeType;

    /** 备注 */
    private String remarks;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Long getMenuId()
    {
        return menuId;
    }

    public void setMenuId(Long menuId)
    {
        this.menuId = menuId;
    }

    public String getEntityName()
    {
        return entityName;
    }

    public void setEntityName(String entityName)
    {
        this.entityName = entityName;
    }

    public String getListMode()
    {
        return listMode;
    }

    public void setListMode(String listMode)
    {
        this.listMode = listMode;
    }

    public String getTreeParentField()
    {
        return treeParentField;
    }

    public void setTreeParentField(String treeParentField)
    {
        this.treeParentField = treeParentField;
    }

    public String getTreeLabelField()
    {
        return treeLabelField;
    }

    public void setTreeLabelField(String treeLabelField)
    {
        this.treeLabelField = treeLabelField;
    }

    public String getTreeNodeType()
    {
        return treeNodeType;
    }

    public void setTreeNodeType(String treeNodeType)
    {
        this.treeNodeType = treeNodeType;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SysMenuMetaMap{");
        sb.append("menuId=").append(menuId);
        sb.append(", entityName=").append(entityName);
        sb.append(", listMode=").append(listMode);
        sb.append(", treeParentField=").append(treeParentField);
        sb.append(", treeLabelField=").append(treeLabelField);
        sb.append(", treeNodeType=").append(treeNodeType);
        sb.append(", remarks=").append(remarks);
        sb.append("}");
        return sb.toString();
    }
}
