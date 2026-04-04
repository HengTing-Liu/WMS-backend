package com.abtk.product.dao.entity;

import com.abtk.product.common.annotation.Excel;
import com.abtk.product.common.annotation.Excel.ColumnType;
import com.abtk.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * (InvQrcodedetail)实体类
 *
 * @author lht
 * @since 2026-03-04 09:41:33
 */
public class InvQrcodedetail extends BaseEntity {
    private static final long serialVersionUID = -74673515390566203L;
    
    /**
     * id
     */
       
    @Excel(name = "id", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "id")
    private Long id;
    
    /**
     * 逻辑删除
     */
       
    @Excel(name = "逻辑删除", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "逻辑删除")
    private Integer isDeleted;
    
    /**
     * qrcode
     */
       
    @Excel(name = "qrcode", type = Type.EXPORT)
    @Schema(description = "qrcode")
    private String qrcode;
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
    
}

