package com.abtk.product.api.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 删除用户请求
 *
 * @description:
 * @author: backend2
 * @time: 2026/3/21
 */
@Data
@Schema(description = "删除用户请求")
public class DeleteUserRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID数组
     */
    @Schema(description = "待删除的用户ID数组", example = "[1, 2, 3]")
    private Long[] userIds;
}
