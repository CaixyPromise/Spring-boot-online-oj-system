package com.caixy.serviceclient.service.user.response;

import cn.hutool.system.UserInfo;
import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.entity.User;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户操作响应
 *
 * @name: com.caixy.serviceclient.service.user.response.UserOperatorResponse
 * @author: CAIXYPROMISE
 * @since: 2024-02-07 00:16
 */
@Getter
@Setter
@NoArgsConstructor
public class UserOperatorResponse extends BaseResponse<UserVO>
{
    public UserOperatorResponse(UserVO usrInfo)
    {
        super(ErrorCode.SUCCESS, usrInfo);
    }

    public UserOperatorResponse(ErrorCode errorCode, UserVO userInfo, String message)
    {
        super(errorCode.getCode(), userInfo, message);
    }

    public static UserOperatorResponse success(UserVO userInfo)
    {
        return new UserOperatorResponse(userInfo);
    }
}
