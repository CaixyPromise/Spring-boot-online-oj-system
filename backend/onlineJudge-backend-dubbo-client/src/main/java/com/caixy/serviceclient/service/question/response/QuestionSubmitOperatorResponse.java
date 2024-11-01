package com.caixy.serviceclient.service.question.response;

import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 题目操作返回类
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.serviceclient.service.question.response.QuestionOperatorResponse
 * @since 2024/9/11 下午3:41
 */
@Getter
@Setter
@NoArgsConstructor
public class QuestionSubmitOperatorResponse extends BaseResponse<Boolean>
{
    public QuestionSubmitOperatorResponse(Boolean data)
    {
        super(ErrorCode.SUCCESS, data);
    }
}
