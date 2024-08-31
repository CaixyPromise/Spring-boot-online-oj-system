package com.caixy.serviceclient.service.user.response;

import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @name: com.caixy.serviceclient.service.user.response.UserQueryResponse
 * @author: CAIXYPROMISE
 * @since: 2024-02-07 00:16
 */
@Setter
@Getter
@ToString
public class UserQueryResponse<T> extends BaseResponse<T>
{

    public UserQueryResponse(ErrorCode errorCode, T data)
    {
        super(errorCode, data);
    }

    public UserQueryResponse(ErrorCode errorCode, T data, String message)
    {
        super(errorCode.getCode(), data, message);
    }

}
