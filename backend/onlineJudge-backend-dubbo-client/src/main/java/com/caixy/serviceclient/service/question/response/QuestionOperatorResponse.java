package com.caixy.serviceclient.service.question.response;

import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 题目操作返回体
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.serviceclient.service.question.response.QuestionOperatorResponse
 * @since 2024/9/12 上午2:41
 */
@Getter
@Setter
@NoArgsConstructor
public class QuestionOperatorResponse<T> extends BaseResponse<T>
{
    public QuestionOperatorResponse(ErrorCode errorCode, T data)
    {
        super(errorCode, data);
    }

    public QuestionOperatorResponse(ErrorCode errorCode, String message, T data)
    {
        super(errorCode.getCode(), data, message);
    }

    public QuestionOperatorResponse(ErrorCode errorCode, String message)
    {
        super(errorCode, message);
    }

    public QuestionOperatorResponse(T data)
    {
        super(ErrorCode.SUCCESS, data);
    }
}
