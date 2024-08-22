package com.caixy.onlineJudge.models.enums.user;

/**
 * 用户身份权限枚举
 *
 * @author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.enums.user；
 * @since 2024-07-12 00:44
 */
public enum UserPermission
{

    /**
     * 基本权限
     */
    BASIC,

    /**
     * 已实名认证权限
     */
    AUTH,

    /**
     * 已冻结权限
     */
    FROZEN,

    /**
     * 无任何权限
     */
    NONE;
}
