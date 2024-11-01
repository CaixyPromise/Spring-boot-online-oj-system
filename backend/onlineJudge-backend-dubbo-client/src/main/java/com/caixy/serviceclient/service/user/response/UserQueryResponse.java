package com.caixy.serviceclient.service.user.response;

import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import lombok.*;

/**
 * @name: com.caixy.serviceclient.service.user.response.UserQueryResponse
 * @author: CAIXYPROMISE
 * @since: 2024-02-07 00:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
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
    private static final long serialVersionUID = 1L;
}
