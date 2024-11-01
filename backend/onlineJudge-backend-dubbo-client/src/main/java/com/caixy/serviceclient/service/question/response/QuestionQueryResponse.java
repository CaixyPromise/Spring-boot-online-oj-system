package com.caixy.serviceclient.service.question.response;

import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.entity.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 题目提交返回体
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.serviceclient.service.question.response.QuestionQueryResponse
 * @since 2024/9/11 下午3:46
 */
@Getter
@Setter
@NoArgsConstructor
public class QuestionQueryResponse<T> extends BaseResponse<T>
{
    public QuestionQueryResponse(T data)
    {
        super(ErrorCode.SUCCESS, data);
    }
}
