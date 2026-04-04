package com.abtk.product.api.domain.response;

import com.abtk.product.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/6 16:45
 *
 */
@Data
@Schema(description = "用户信息响应出参")
public class SysUserResponse extends BaseResponse {
    /** 用户ID */
    @Excel(name = "用户序号", type = Excel.Type.EXPORT, cellType = Excel.ColumnType.NUMERIC, prompt = "用户编号")
    @Schema(description = "用户唯一标识", example = "1001")
    private Long userId;

    /** 部门ID */
    @Excel(name = "部门编号", type = Excel.Type.IMPORT)
    @Schema(description = "所属部门ID", example = "101")
    private Long deptId;

    /** 部门名称 */
    @Excel(name = "部门名称", type = Excel.Type.IMPORT)
    @Schema(description = "所属部门名称", example = "研发部")
    private String deptName;

    /** 用户账号 */
    @Excel(name = "登录名称")
    @Schema(description = "登录用户名（唯一）", example = "admin")
    private String userName;

    /** 用户昵称 */
    @Excel(name = "用户名称")
    @Schema(description = "用户显示名称/昵称", example = "系统管理员")
    private String nickName;

    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    @Schema(description = "用户邮箱地址", example = "admin@example.com")
    private String email;

    /** 手机号码 */
    @Excel(name = "手机号码", cellType = Excel.ColumnType.TEXT)
    @Schema(description = "手机号码", example = "13800138000")
    private String phonenumber;

    /** 用户性别 */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    @Schema(description = "性别：0=男，1=女，2=未知", allowableValues = {"0", "1", "2"}, example = "0")
    private String sex;

    /** 账号状态（0正常 1停用） */
    @Excel(name = "账号状态", readConverterExp = "0=正常,1=停用")
    @Schema(description = "账号状态：0=正常，1=停用", allowableValues = {"0", "1"}, example = "0")
    private String status;

    /** 角色ID（通常用于查询条件） */
    @Schema(description = "指定角色ID（用于筛选）", example = "1")
    private Long roleId;
}
