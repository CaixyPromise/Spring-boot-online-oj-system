package com.caixy.onlineJudge.auth.manager.oauth;

import cn.hutool.core.util.RandomUtil;
import com.caixy.onlineJudge.auth.annotation.InjectOAuthConfig;
import com.caixy.onlineJudge.auth.annotation.OAuthTypeTarget;
import com.caixy.onlineJudge.auth.config.properties.OAuth2ClientProperties;
import com.caixy.onlineJudge.auth.strategy.OAuth2ActionStrategy;
import com.caixy.onlineJudge.common.base.utils.HttpUtils;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.json.JsonUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.dto.oauth.OAuthResultDTO;
import com.caixy.onlineJudge.models.dto.oauth.github.GithubCallbackRequest;
import com.caixy.onlineJudge.models.dto.oauth.github.GithubCallbackResponse;
import com.caixy.onlineJudge.models.dto.oauth.github.GithubGetAuthorizationUrlRequest;
import com.caixy.onlineJudge.models.dto.oauth.github.GithubUserProfileDTO;
import com.caixy.onlineJudge.models.dto.user.UserLoginByOAuthAdapter;
import com.caixy.onlineJudge.models.entity.User;
import com.caixy.onlineJudge.models.enums.oauth2.OAuthProviderEnum;
import com.caixy.onlineJudge.models.enums.redis.RedisKeyEnum;
import com.caixy.onlineJudge.models.enums.user.UserGenderEnum;
import com.caixy.onlineJudge.models.enums.user.UserRoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Github的OAuth2验证实现
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.auth.manager.oauth.GithubOAuthActionStrategyImpl
 * @since 2024/8/6 上午1:38
 */
@Slf4j
@Component
@OAuthTypeTarget(clientName = OAuthProviderEnum.GITHUB)
public class GithubOAuthActionStrategyImpl extends OAuth2ActionStrategy<
        GithubCallbackResponse,
        GithubUserProfileDTO,
        GithubGetAuthorizationUrlRequest,
        GithubCallbackRequest>
{
    @InjectOAuthConfig(clientName = OAuthProviderEnum.GITHUB)
    private OAuth2ClientProperties.OAuth2Client githubClient;

    private static final String STATE_KEY = "state";
    private static final String USER_REDIRECT_URI_KEY = "user_redirect_uri";


    @Override
    public String getAuthorizationUrl(GithubGetAuthorizationUrlRequest authorizationUrlRequest)
    {
        String state = RandomUtil.randomNumbers(5);
        String url = githubClient.getAuthServerUrl() +
                     "?client_id=" + githubClient.getClientId() +
                     "&redirect_uri=" + githubClient.getCallBackUrl() +
                     "&state=" + state;
        HashMap<String, String> stateMap = new HashMap<>();
        stateMap.put(STATE_KEY, state);
        stateMap.put(USER_REDIRECT_URI_KEY, authorizationUrlRequest.getRedirectUri());
        log.info("stateMap: {}", stateMap);
        log.info("authorizationUrlRequest: {}", authorizationUrlRequest);
        log.info("url: {}", url);
        redisUtils.setHashMap(RedisKeyEnum.GITHUB_OAUTH, stateMap, authorizationUrlRequest.getSessionId());
        return url;
    }

    @Override
    public GithubCallbackResponse doCallback(GithubCallbackRequest callbackRequest)
    {
        Map<String, Object> authorizationMap = redisUtils.getHashMap(RedisKeyEnum.GITHUB_OAUTH, String.class,
                Object.class,
                callbackRequest.getSessionId());
        if (authorizationMap == null)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "授权已过期或无效");
        }
        String stateKey = authorizationMap.get(STATE_KEY).toString().replaceAll("\"", "");
        log.info("stateKey:{}, callbackRequest.getState(): {}", stateKey, callbackRequest.getState());
        if (StringUtils.isBlank(stateKey) || !stateKey.equals(callbackRequest.getState()))
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "授权已过期或无效");
        }
        String accessToken = getAccessToken(callbackRequest.getCode());

        GithubCallbackResponse.GithubCallbackResponseBuilder responseBuilder = GithubCallbackResponse.builder();
        responseBuilder.redirectUri((String) authorizationMap.get(USER_REDIRECT_URI_KEY));
        responseBuilder.accessToken(accessToken);
        return responseBuilder.build();
    }

    @Override
    public GithubUserProfileDTO getUserProfile(GithubCallbackResponse callbackResponse)
    {
        GithubUserProfileDTO githubUserProfileDTO = fetchGitHubUserProfile(callbackResponse.getAccessToken());
        if (githubUserProfileDTO == null || githubUserProfileDTO.getMessage() != null)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取用户信息失败");
        }
        return githubUserProfileDTO;
    }

    @Override
    public OAuthResultDTO doAuth(Map<String, Object> paramMaps)
    {
        GithubCallbackRequest githubCallbackRequest = safetyConvertMapToObject(paramMaps, GithubCallbackRequest.class);
        GithubCallbackResponse githubCallbackResponse = doCallback(githubCallbackRequest);
        GithubUserProfileDTO userProfile = getUserProfile(githubCallbackResponse);
        if (userProfile == null)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取用户信息失败");
        }
        OAuthResultDTO.OAuthResultDTOBuilder resultBuilder = OAuthResultDTO.builder();
        resultBuilder.result(userProfile)
                     .success(true)
                     .redirectUrl(githubCallbackResponse.getRedirectUri())
                     .loginAdapter(getUserLoginByOAuthAdapter(userProfile));
        return resultBuilder.build();
    }

    private UserLoginByOAuthAdapter getUserLoginByOAuthAdapter(GithubUserProfileDTO userProfileDTO)
    {
        UserLoginByOAuthAdapter.UserLoginByOAuthAdapterBuilder adapterBuilder = UserLoginByOAuthAdapter.builder();
        User.UserBuilder userBuilder = User.builder();
        userBuilder.userEmail(userProfileDTO.getEmail())
                   .githubId(userProfileDTO.getId())
                   .userAvatar(userProfileDTO.getAvatarUrl())
                   .userGender(UserGenderEnum.UNKNOWN.getValue())
                   .userRole(UserRoleEnum.USER.getValue())
                   .nickName(userProfileDTO.getName());

        adapterBuilder.uniqueFieldName("githubId")
                      .uniqueFieldValue(userProfileDTO.getId())
                      .userInfo(userBuilder.build());
        return adapterBuilder.build();
    }

    private String getAccessToken(String code)
    {
        String host = githubClient.getAccessTokenUrl();
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        Map<String, String> bodys = new HashMap<>();
        bodys.put("client_id", githubClient.getClientId());
        bodys.put("client_secret", githubClient.getClientSecret());
        bodys.put("code", code);
        bodys.put("grant_type", "authorization_code");

        try
        {
            HttpResponse response = HttpUtils.doPost(host, headers, null, bodys);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
            {
                // 错误相应
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "验证失败");
            }
            String responseStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            log.info("getAccessToken response: {}", responseStr);

            Map<String, Object> responseMap = JsonUtils.jsonToMap(responseStr);
            return (String) responseMap.get("access_token");
        }
        catch (IOException e)
        {
            log.error("Error while getting access token: ", e);
            return null;
        }
    }

    private GithubUserProfileDTO fetchGitHubUserProfile(String accessToken)
    {
        String host = githubClient.getFetchInfoUrl();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "token " + accessToken);
        headers.put("Accept", "application/json");

        try
        {
            HttpResponse response = HttpUtils.doGet(host, headers, null);

            String userInfo = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
            {
                // 错误相应
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "验证失败");
            }
            log.info("fetchGitHubUserProfile: {}", userInfo);

            return JsonUtils.jsonToObject(userInfo, GithubUserProfileDTO.class);
        }
        catch (IOException e)
        {
            log.error("Error while fetching user profile: ", e);
            return null;
        }
    }
}
