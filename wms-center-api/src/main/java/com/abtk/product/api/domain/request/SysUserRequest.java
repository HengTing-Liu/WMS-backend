package com.abtk.product.api.domain.request;

import com.abtk.product.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/6 10:23
 *
 */
@Data
@Schema(description = "用户信息请求入参")
public class SysUserRequest extends BaseRequest {
    /** 用户ID */
    @Excel(name = "用户序号", type = Excel.Type.EXPORT, cellType = Excel.ColumnType.NUMERIC, prompt = "用户编号")
    @Schema(description = "用户唯一标识", example = "1001")
    private Long userId;

    /** 用户账号 */
    @Excel(name = "登录名称")
    @Schema(description = "登录用户名（唯一）", example = "admin")
    private String userName;

    @Excel(name = "密码")
    @Schema(description = "登录用户密码", example = "admin")
    private String password;

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

    /** 账号状态（0正常 1停用） */
    @Excel(name = "账号状态", readConverterExp = "0=正常,1=停用")
    @Schema(description = "账号状态：0=正常，1=停用", allowableValues = {"0", "1"}, example = "0")
    private String status;

    @Schema(description = "入职日期")
    private Date entryDate;

    @Schema(description = "离职日期")
    private Date leaveDate;

    /** 部门编码（与 sys_user.dept_code / sys_dept.dept_code 对应；已移除 sys_user.dept_id） */
    @Schema(description = "部门编码 dept_code", example = "D001")
    private String deptCode;

    /** 角色组（仅用于请求） */
    @Schema(description = "分配的角色ID列表", accessMode = Schema.AccessMode.WRITE_ONLY, example = "[1, 2, 3]")
    private Long[] roleIds;

    /** 岗位组（仅用于请求） */
    @Schema(description = "分配的岗位ID列表", accessMode = Schema.AccessMode.WRITE_ONLY, example = "[10, 20]")
    private Long[] postIds;

    /** 角色ID（通常用于查询条件） */
    @Schema(description = "指定角色ID（用于筛选）", example = "1")
    private Long roleId;
}
