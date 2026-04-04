package com.abtk.product.api.domain.response;

import lombok.Data;

import java.util.Set;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/3 19:30
 *
 */
@Data
public class LoginUserResponse {
    /**
     * 用户头像 URL
     */
    private String avatar;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户角色列表（可选）
     */
    private Set<String> roles;

    /**
     * 用户ID（注意：前端传的是 string，但后端通常是 Long）
     * 若需与后端 Long 类型对齐，可额外提供 getUserIdAsLong()
     */
    private String userId;

    /**
     * 用户名（登录账号）
     */
    private String username;

    /**
     * 用户描述/简介
     */
    private String desc;

    /**
     * 首页路径（登录后跳转地址）
     */
    private String homePath;

    /**
     * 认证令牌（JWT Token 等）
     */
    private String token;

    /**
     * 权限
     */
    private Set<String> permissions;

}
