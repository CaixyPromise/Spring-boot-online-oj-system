package com.caixy.onlineJudge.models.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 已登录用户视图（脱敏）
 **/
@Data
public class LoginUserVO implements Serializable
{
    /**
     * 用户 id
     */
    private Long id;

    /**
     * token名称
     */
    private String token;

    /**
     * 令牌过期时间
     */
    private Long tokenExpiration;

    private static final long serialVersionUID = 1L;
}