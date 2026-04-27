package com.abtk.product.api.domain.request.user;

import com.abtk.product.api.domain.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 用户管理请求入参
 *
 * @author backend
 * @since 2026-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户管理请求入参")
public class WmsUserRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long userId;

    /**
     * 用户账号（登录名）
     */
    @NotBlank(message = "用户账号不能为空")
    @Size(max = 30, message = "用户账号长度不能超过30个字符")
    @Schema(description = "用户账号（登录名）")
    private String userName;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    @Size(max = 30, message = "用户昵称长度不能超过30个字符")
    @Schema(description = "用户昵称")
    private String nickName;

    /**
     * 用户邮箱
     */
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    @Schema(description = "用户邮箱")
    private String email;

    /**
     * 手机号码
     */
    @Size(max = 11, message = "手机号码长度不能超过11个字符")
    @Schema(description = "手机号码")
    private String phonenumber;

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
    @Schema(description = "账号状态：0=正常，1=停用")
    private String status;

    @Schema(description = "离职日期")
    private Date leaveDate;

    /**
     * 默认首页
     */
    @Schema(description = "默认首页")
    private String defaultPage;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    @Schema(description = "备注")
    private String remarks;
}
