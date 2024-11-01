package com.caixy.onlineJudge.business.question.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.onlineJudge.business.question.service.QuestionService;
import com.caixy.onlineJudge.business.question.service.QuestionSubmitService;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.exception.ThrowUtils;
import com.caixy.onlineJudge.common.json.JsonUtils;
import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.common.response.ResultUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.constants.common.UserConstant;
import com.caixy.onlineJudge.models.common.DeleteRequest;
import com.caixy.onlineJudge.models.dto.question.*;
import com.caixy.onlineJudge.models.dto.submit.QuestionSubmitAddRequest;
import com.caixy.onlineJudge.models.dto.submit.QuestionSubmitQueryRequest;
import com.caixy.onlineJudge.models.entity.Question;
import com.caixy.onlineJudge.models.entity.QuestionSubmit;
import com.caixy.onlineJudge.models.enums.user.UserRoleEnum;
import com.caixy.onlineJudge.models.vo.quetion.QuestionVO;
import com.caixy.onlineJudge.models.vo.submit.QuestionSubmitVO;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import com.caixy.onlineJudge.rabbitmq.enums.RabbitMQQueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 题目接口控制器
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.question.controller.QuestionController
 * @since 2024/9/11 上午1:47
 */
@Slf4j
@RestController
@RequestMapping("/")
public class QuestionController
{
    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;


    // region 增删改查

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Question> getQuestionById(long id, HttpServletRequest request)
    {
        if (id <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        UserVO loginUser = getLoginUser();
        // 不是本人或管理员，不能直接获取所有信息
        if (!question.getCreatorId().equals(loginUser.getId()) && !loginUser.getUserRole().equals(UserRoleEnum.ADMIN))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(question);
    }

    /**
     * 根据 id 获取（脱敏）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(long id, HttpServletRequest request)
    {
        if (id <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionService.getQuestionVO(question, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                               HttpServletRequest request)
    {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                                 HttpServletRequest request)
    {
        if (questionQueryRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVO loginUser = getLoginUser();
        questionQueryRequest.setUserId(loginUser.getId());
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    // endregion

    /**
     * 编辑（用户）
     *
     * @param questionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest,
                                              HttpServletRequest request)
    {
        if (questionEditRequest == null || questionEditRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);
        List<String> tags = questionEditRequest.getTags();
        if (tags != null)
        {
            question.setTags(JsonUtils.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionEditRequest.getJudgeCase();
        if (judgeCase != null)
        {
            question.setJudgeCase(JsonUtils.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionEditRequest.getJudgeConfig();
        if (judgeConfig != null)
        {
            question.setJudgeConfig(JsonUtils.toJson(judgeConfig));
        }
        // 参数校验
        questionService.validQuestion(question, false);
        UserVO loginUser = getLoginUser();
        long id = questionEditRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestion.getCreatorId().equals(loginUser.getId()) && !loginUser.getUserRole().equals(
                UserRoleEnum.ADMIN))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return 提交记录的 id
     */
    @PostMapping("/question_submit/do")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request)
    {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能
        UserVO loginUser = getLoginUser();
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }

    /**
     * 分页获取题目提交列表（除了管理员外，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @PostMapping("/question_submit/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(
            @RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest)
    {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        // 从数据库中查询原始的题目提交分页信息
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));

        // 返回脱敏信息
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, getLoginUser()));
    }

    private UserVO getLoginUser()
    {
        Object loginId = StpUtil.getLoginId();
        return (UserVO) StpUtil.getSessionByLoginId(loginId).get((String) loginId);
    }

}
