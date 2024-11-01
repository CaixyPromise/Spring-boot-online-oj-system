package com.caixy.onlineJudge.business.judge.service;

import com.caixy.onlineJudge.business.judge.factory.SandBoxProviderFactory;
import com.caixy.onlineJudge.business.judge.manager.JudgeManager;
import com.caixy.onlineJudge.business.judge.manager.core.JudgeContext;
import com.caixy.onlineJudge.business.judge.sandbox.CodeSandBox;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.json.JsonUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.dto.question.JudgeCase;
import com.caixy.onlineJudge.models.dto.sandbox.ExecuteCodeRequest;
import com.caixy.onlineJudge.models.dto.sandbox.ExecuteCodeResponse;
import com.caixy.onlineJudge.models.dto.sandbox.JudgeInfo;
import com.caixy.onlineJudge.models.entity.Question;
import com.caixy.onlineJudge.models.entity.QuestionSubmit;
import com.caixy.onlineJudge.models.enums.question.QuestionSubmitStatusEnum;
import com.caixy.onlineJudge.models.enums.sandbox.LanguageProviderEnum;
import com.caixy.serviceclient.service.question.QuestionFacadeService;
import com.caixy.serviceclient.service.question.response.QuestionQueryResponse;
import com.caixy.serviceclient.service.question.response.QuestionSubmitOperatorResponse;
import com.caixy.serviceclient.service.question.response.QuestionSubmitQueryResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题服务实现
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.judge.service.impl.JudgeServiceImpl
 * @since 2024/9/11 下午4:33
 */
@Service
public class JudgeService
{
    @Resource
    private QuestionFacadeService questionFacadeService;

    @Resource
    private SandBoxProviderFactory sandBoxProviderFactory;

    @Resource
    private JudgeManager judgeManager;

    public QuestionSubmit handleSubmit(Long submitId)
    {
        // 获取提交和题目信息，并检查状态
        QuestionSubmit questionSubmit = validateAndGetQuestionSubmit(submitId);
        Question question = validateAndGetQuestion(questionSubmit.getQuestionId());

        // 更新提交状态为"判题中"
        updateSubmitStatus(submitId, QuestionSubmitStatusEnum.RUNNING.getValue());

        // 调用沙箱执行代码
        ExecuteCodeResponse executeCodeResponse = executeCodeInSandBox(questionSubmit, question);

        // 根据执行结果判题
        JudgeInfo judgeInfo = judgeAndGetInfo(questionSubmit, question, executeCodeResponse);

        // 更新判题结果
        updateJudgeResult(submitId, judgeInfo);

        return questionFacadeService.getQuestionSubmitById(questionSubmit.getQuestionId()).getData();
    }

    private QuestionSubmit validateAndGetQuestionSubmit(Long submitId)
    {
        QuestionSubmitQueryResponse submitResponse = questionFacadeService.getQuestionSubmitById(submitId);
        QuestionSubmit questionSubmit = submitResponse.getData();
        if (questionSubmit == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue()))
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        return questionSubmit;
    }

    private Question validateAndGetQuestion(Long questionId)
    {
        QuestionQueryResponse<Question> questionResponse = questionFacadeService.getQuestionById(questionId);
        Question question = questionResponse.getData();
        if (question == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        return question;
    }

    private void updateSubmitStatus(Long submitId, Integer status)
    {
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(submitId);
        questionSubmitUpdate.setStatus(status);
        boolean update = questionFacadeService.updateQuestionSubmit(questionSubmitUpdate).isSucceed();
        if (!update)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
    }

    private ExecuteCodeResponse executeCodeInSandBox(QuestionSubmit questionSubmit, Question question)
    {
        String language = questionSubmit.getLanguage();
        LanguageProviderEnum providerEnum = LanguageProviderEnum.getProviderEnumByValue(language);
        if (providerEnum == null)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "语言不支持");
        }
        CodeSandBox sandBox = sandBoxProviderFactory.getInstance(providerEnum);
        if (sandBox == null)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "语言不支持");
        }
        String code = questionSubmit.getCode();
        List<String> inputList = getInputListFromQuestion(question);

        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                                                                  .code(code)
                                                                  .language(language)
                                                                  .inputList(inputList)
                                                                  .userId(questionSubmit.getUserId())
                                                                  .build();

        return sandBoxProviderFactory.doExecute(executeCodeRequest, sandBox);
    }

    private List<String> getInputListFromQuestion(Question question)
    {
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JsonUtils.toList(judgeCaseStr);
        return judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
    }

    private JudgeInfo judgeAndGetInfo(QuestionSubmit questionSubmit, Question question,
                                      ExecuteCodeResponse executeCodeResponse)
    {
        List<String> outputList = executeCodeResponse.getOutputList();
        List<JudgeCase> judgeCaseList = JsonUtils.toList(question.getJudgeCase());

        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(getInputListFromQuestion(question));
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);

        LanguageProviderEnum languageProviderEnum = LanguageProviderEnum.getProviderEnumByValue(
                questionSubmit.getLanguage());
        return judgeManager.doJudge(languageProviderEnum, judgeContext);
    }

    private void updateJudgeResult(Long submitId, JudgeInfo judgeInfo)
    {
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(submitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JsonUtils.toJson(judgeInfo));
        boolean update = questionFacadeService.updateQuestionSubmit(questionSubmitUpdate).isSucceed();
        if (!update)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
    }
}
