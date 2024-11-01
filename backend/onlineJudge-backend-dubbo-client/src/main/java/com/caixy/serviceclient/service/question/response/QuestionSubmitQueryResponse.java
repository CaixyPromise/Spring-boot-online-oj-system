package com.caixy.serviceclient.service.question.response;

import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.entity.QuestionSubmit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 获取题目提交信息操作体
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.serviceclient.service.question.response.QuestionSubmitQueryResponse
 * @since 2024/9/11 下午3:44
 */
@Getter
@Setter
@NoArgsConstructor
public class QuestionSubmitQueryResponse extends BaseResponse<QuestionSubmit>
{
    public QuestionSubmitQueryResponse(QuestionSubmit data)
    {
        super(ErrorCode.SUCCESS, data);
    }
}
