package com.abclonal.product.dao.entity;

import com.abclonal.product.common.annotation.Excel;
import com.abclonal.product.common.annotation.Excel.ColumnType;
import com.abclonal.product.common.annotation.Excel.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户管理表(WmsUser)实体类
 *
 * @author backend
 * @since 2026-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户管理表")
public class WmsUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Excel(name = "用户序号", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    @Schema(description = "主键ID")
    private Long userId;

    /**
     * 部门ID
     */
    @Excel(name = "部门编号", type = Type.IMPORT)
    @Schema(description = "部门ID")
    private Long deptId;

    /**
     * 用户账号（登录名）
     */
    @Excel(name = "登录名称")
    @Schema(description = "用户账号（登录名）")
    private String userName;

    /**
     * 用户昵称
     */
    @Excel(name = "用户名称")
    @Schema(description = "用户昵称")
    private String nickName;

    /**
     * 用户邮箱
     */
    @Excel(name = "用户邮箱")
    @Schema(description = "用户邮箱")
    private String email;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码", cellType = ColumnType.TEXT)
    @Schema(description = "手机号码")
    private String phonenumber;

    /**
     * 用户性别：0=男，1=女，2=未知
     */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    @Schema(description = "用户性别：0=男，1=女，2=未知")
    private String sex;

    /**
     * 头像地址
     */
    @Schema(description = "头像地址")
    private String avatar;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 账号状态：0=正常，1=停用
     */
    @Excel(name = "账号状态", readConverterExp = "0=正常,1=停用")
    @Schema(description = "账号状态：0=正常，1=停用")
    private String status;

    /**
     * 删除标志（0代表存在，2代表删除）
     */
    @Schema(description = "删除标志（0代表存在，2代表删除）")
    private String delFlag;

    /**
     * 最后登录IP
     */
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    @Schema(description = "最后登录IP")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    @Schema(description = "最后登录时间")
    private Date loginDate;

    /**
     * 密码最后更新时间
     */
    @Schema(description = "密码最后更新时间")
    private Date pwdUpdateDate;
}
