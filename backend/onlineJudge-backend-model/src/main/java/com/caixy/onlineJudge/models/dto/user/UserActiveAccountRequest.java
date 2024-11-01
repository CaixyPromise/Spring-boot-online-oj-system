package com.caixy.onlineJudge.models.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户激活账号请求体
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.user.UserActiveAccountRequest
 * @since 2024/9/6 上午2:14
 */
@Data
public class UserActiveAccountRequest implements Serializable
{
    /**
     * 激活token
     */
    @NotNull
    private String activeToken;
    /**
     * 用户查询姓名临时token
     */
    @NotNull
    private String tempToken;
    /**
     * 用户名
     */
    @NotNull
    private String nickName;
    /**
     * 用户性别
     */
    @NotNull
    private Integer userGender;
    private static final long serialVersionUID = 1L;
}
