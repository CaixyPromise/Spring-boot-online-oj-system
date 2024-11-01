package com.caixy.onlineJudge.business.question.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.onlineJudge.business.question.service.QuestionService;
import com.caixy.onlineJudge.business.question.service.QuestionSubmitService;
import com.caixy.onlineJudge.common.rpc.facade.Facade;
import com.caixy.onlineJudge.models.entity.Question;
import com.caixy.onlineJudge.models.entity.QuestionSubmit;
import com.caixy.serviceclient.service.question.QuestionFacadeService;
import com.caixy.serviceclient.service.question.response.QuestionOperatorResponse;
import com.caixy.serviceclient.service.question.response.QuestionQueryResponse;
import com.caixy.serviceclient.service.question.response.QuestionSubmitOperatorResponse;
import com.caixy.serviceclient.service.question.response.QuestionSubmitQueryResponse;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 题目远程调用服务实现类
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.question.facade.QuestionFacadeServiceImpl
 * @since 2024/9/11 下午3:48
 */
@DubboService(version = "1.0.0")
public class QuestionFacadeServiceImpl implements QuestionFacadeService
{
    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    /**
     * 根据题目获取题目
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/11 下午3:52
     */
    @Facade
    @Override
    public QuestionQueryResponse<Question> getQuestionById(Long id)
    {
        return new QuestionQueryResponse<>(questionService.getQuestionById(id));
    }

    /**
     * 获取题目提交信息，用于判题获取提交信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/11 下午3:51
     */
    @Facade
    @Override
    public QuestionSubmitQueryResponse getQuestionSubmitById(Long id)
    {
        return new QuestionSubmitQueryResponse(questionSubmitService.getQuestionSubmitById(id));
    }

    /**
     * 更新题目提交信息，用于更新题目判题状态信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/11 下午3:51
     */
    @Facade
    @Override
    public QuestionSubmitOperatorResponse updateQuestionSubmit(QuestionSubmit questionSubmit)
    {
        return new QuestionSubmitOperatorResponse(questionSubmitService.updateQuestionSubmit(questionSubmit));
    }


    /**
     * 保存题目远程调用
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/12 上午2:37
     */
    @Facade
    @Override
    public QuestionOperatorResponse<Boolean> doSaveQuestion(Question question)
    {
        return questionService.doSave(question);
    }

    /**
     * 更新题目远程调用
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/12 上午2:54
     */
    @Facade
    @Override
    public QuestionOperatorResponse<Question> doUpdateQuestion(Question question)
    {
        return questionService.doUpdate(question);
    }

    /**
     * 删除题目远程调用
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/12 上午2:54
     */
    @Facade
    @Override
    public QuestionOperatorResponse<Boolean> doDeleteQuestion(Long id)
    {
        return questionService.doDelete(id);
    }

    /**
     * 分页获取题目
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/12 上午3:01
     */
    @Facade
    @Override
    public QuestionQueryResponse<Page<Question>> getQuestionPage(Long current, Long size)
    {
        return questionService.getQuestionPage(current, size);
    }
}
