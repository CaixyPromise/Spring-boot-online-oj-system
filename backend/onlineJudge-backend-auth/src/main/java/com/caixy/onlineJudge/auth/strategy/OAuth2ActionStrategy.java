package com.caixy.onlineJudge.auth.strategy;

import com.caixy.onlineJudge.common.cache.redis.RedisUtils;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.jackson.ObjectMapperUtil;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.dto.oauth.OAuthResultDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Resource;
import java.util.Map;

/**
 * OAuth服务端策略
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.auth.strategy.OAuth2ActionStrategy
 * @since 2024/8/6 上午1:35
 */
public abstract class OAuth2ActionStrategy<
        CallbackPayloadResponseType, // 回调参数类型
        UserProfileType,    // 用户信息类型
        GetAuthorizationUrlRequestType, // 获取授权URL类型
        GetCallbackRequestType>    // 获取回调请求类型
{
    @Resource
    protected RedisUtils redisUtils;

    public abstract String getAuthorizationUrl(GetAuthorizationUrlRequestType authorizationUrlType);

    public abstract CallbackPayloadResponseType doCallback(GetCallbackRequestType callbackType);

    public abstract UserProfileType getUserProfile(CallbackPayloadResponseType callback);

    public abstract OAuthResultDTO doAuth(Map<String, Object> paramMaps);

    protected <T> T safetyConvertMapToObject(Map<String, Object> paramMaps, Class<T> clazz)
    {
        T convertValue = ObjectMapperUtil.convertValue(paramMaps, clazz);
        if (convertValue == null)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "授权失败");
        }
        return convertValue;
    }
}