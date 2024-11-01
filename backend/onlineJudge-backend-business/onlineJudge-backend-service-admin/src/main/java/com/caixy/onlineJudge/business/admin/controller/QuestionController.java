package com.caixy.onlineJudge.business.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.json.JsonUtils;
import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.common.response.ResultUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.common.DeleteRequest;
import com.caixy.onlineJudge.models.dto.question.*;
import com.caixy.onlineJudge.models.entity.Question;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import com.caixy.serviceclient.service.question.QuestionFacadeService;
import com.caixy.serviceclient.service.question.response.QuestionOperatorResponse;
import com.caixy.serviceclient.service.question.response.QuestionQueryResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 题目接口控制器
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.admin.controller.QuestionController
 * @since 2024/9/12 上午2:29
 */
@RestController
@RequestMapping("/question")
public class QuestionController
{
    @Resource
    private QuestionFacadeService questionFacadeService;

    /**
     * 创建
     *
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addQuestion(@RequestBody QuestionAddRequest questionAddRequest,
                                          HttpServletRequest request)
    {
        if (questionAddRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        List<String> tags = questionAddRequest.getTags();
        if (tags != null)
        {
            question.setTags(JsonUtils.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase();
        if (judgeCase != null)
        {
            question.setJudgeCase(JsonUtils.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
        if (judgeConfig != null)
        {
            question.setJudgeConfig(JsonUtils.toJson(judgeConfig));
        }
        QuestionOperatorResponse<Boolean> saveQuestion = questionFacadeService.doSaveQuestion(
                question);
        return ResultUtils.success(saveQuestion.isSucceed());
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        QuestionOperatorResponse<Boolean> deleteQuestion = questionFacadeService.doDeleteQuestion(id);
        return ResultUtils.success(deleteQuestion.isSucceed());
    }

    /**
     * 更新（仅管理员）
     *
     * @param questionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest)
    {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        List<String> tags = questionUpdateRequest.getTags();
        if (tags != null)
        {
            question.setTags(JsonUtils.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionUpdateRequest.getJudgeCase();
        if (judgeCase != null)
        {
            question.setJudgeCase(JsonUtils.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionUpdateRequest.getJudgeConfig();
        if (judgeConfig != null)
        {
            question.setJudgeConfig(JsonUtils.toJson(judgeConfig));
        }
        // 参数校验
        QuestionOperatorResponse<Question> updateQuestion = questionFacadeService.doUpdateQuestion(question);
        return ResultUtils.success(updateQuestion.isSucceed());
    }

    /**
     * 分页获取题目列表（仅管理员）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                           HttpServletRequest request)
    {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        QuestionQueryResponse<Page<Question>> queryPageResponse = questionFacadeService.getQuestionPage(current, size);
        Page<Question> questionPage = queryPageResponse.getData();
        return ResultUtils.success(questionPage);
    }
}
