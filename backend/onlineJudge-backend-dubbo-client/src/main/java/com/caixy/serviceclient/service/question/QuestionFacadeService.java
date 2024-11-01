package com.caixy.serviceclient.service.question;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.onlineJudge.models.entity.Question;
import com.caixy.onlineJudge.models.entity.QuestionSubmit;
import com.caixy.serviceclient.service.question.response.QuestionOperatorResponse;
import com.caixy.serviceclient.service.question.response.QuestionQueryResponse;
import com.caixy.serviceclient.service.question.response.QuestionSubmitOperatorResponse;
import com.caixy.serviceclient.service.question.response.QuestionSubmitQueryResponse;

/**
 * 题目远程调用接口
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.serviceclient.service.quetion.QuestionFacadeService
 * @since 2024/9/11 下午3:39
 */
public interface QuestionFacadeService
{
    /**
     * 根据id获取题目
     */
    QuestionQueryResponse<Question> getQuestionById(Long id);

    /**
     * 根据id获取题目提交信息
     */
    QuestionSubmitQueryResponse getQuestionSubmitById(Long id);

    /**
     * 更新题目提交信息
     */
    QuestionSubmitOperatorResponse updateQuestionSubmit(QuestionSubmit questionSubmit);

    QuestionOperatorResponse<Boolean> doSaveQuestion(Question question);

    QuestionOperatorResponse<Question> doUpdateQuestion(Question question);

    QuestionOperatorResponse<Boolean> doDeleteQuestion(Long id);

    QuestionQueryResponse<Page<Question>> getQuestionPage(Long current, Long size);
}
