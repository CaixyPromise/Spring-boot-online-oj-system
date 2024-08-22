package com.caixy.onlineJudge.models.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.enums.user.UserStateEnum
 * @since 2024/8/11 上午2:35
 */
@Getter
@AllArgsConstructor
public enum UserStateEnum
{
    ACTIVE("正常", 1),
    DISABLED("禁用", 0)

    ;
    private final String name;
    private final int code;

    public static UserStateEnum getEnumByCode(Integer code)
    {
        if (code == null)
        {
            return null;
        }
        for (UserStateEnum userStateEnum : values())
        {
            if (userStateEnum.getCode() == code)
            {
                return userStateEnum;
            }
        }
        return null;
    }
}
