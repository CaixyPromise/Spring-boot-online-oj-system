package com.caixy.onlineJudge.models.dto.user;

import com.caixy.onlineJudge.models.dto.oauth.OAuthResultDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户OAuth远程调用请求体
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.user.UserOAuthRequest
 * @since 2024/8/11 上午1:30
 */
@Data
public class UserOAuthRequest implements Serializable
{
    /**
     * OAuth返回结果
     */
    private OAuthResultDTO oAuthResultDTO;

    private static final long serialVersionUID = 1L;
}
