package com.abclonal.product.api.domain.response.user;

import com.abclonal.product.api.domain.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户管理响应出参
 *
 * @author backend
 * @since 2026-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户管理响应出参")
public class WmsUserResponse extends BaseResponse {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long userId;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    private Long deptId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String deptName;

    /**
     * 用户账号（登录名）
     */
    @Schema(description = "用户账号（登录名）")
    private String userName;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String nickName;

    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱")
    private String email;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码")
    private String phonenumber;

    /**
     * 用户性别：0=男，1=女，2=未知
     */
    @Schema(description = "用户性别：0=男，1=女，2=未知")
    private String sex;

    /**
     * 头像地址
     */
    @Schema(description = "头像地址")
    private String avatar;

    /**
     * 账号状态：0=正常，1=停用
     */
    @Schema(description = "账号状态：0=正常，1=停用")
    private String status;

    /**
     * 最后登录IP
     */
    @Schema(description = "最后登录IP")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @Schema(description = "最后登录时间")
    private String loginDate;

    /**
     * 默认首页
     */
    @Schema(description = "默认首页")
    private String defaultPage;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
