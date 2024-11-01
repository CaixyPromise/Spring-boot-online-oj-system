package com.caixy.onlineJudge.business.question.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.onlineJudge.business.question.mapper.QuestionMapper;
import com.caixy.onlineJudge.business.question.service.QuestionService;
import com.caixy.onlineJudge.common.base.utils.SqlUtils;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.exception.ThrowUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.constants.common.CommonConstant;
import com.caixy.onlineJudge.models.convertor.user.UserConvertor;
import com.caixy.onlineJudge.models.dto.question.QuestionQueryRequest;
import com.caixy.onlineJudge.models.entity.Question;

import com.caixy.onlineJudge.models.entity.User;
import com.caixy.onlineJudge.models.enums.user.UserRoleEnum;
import com.caixy.onlineJudge.models.vo.quetion.QuestionVO;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import com.caixy.serviceclient.service.question.response.QuestionOperatorResponse;
import com.caixy.serviceclient.service.question.response.QuestionQueryResponse;
import com.caixy.serviceclient.service.user.UserFacadeService;
import com.caixy.serviceclient.service.user.request.UserQueryFacadeRequest;
import com.caixy.serviceclient.service.user.request.condition.UserIdQueryCondition;
import com.caixy.serviceclient.service.user.response.UserQueryResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author CAIXYPROMISE
 * @description 针对表【question(题目)】的数据库操作Service实现
 * @createDate 2024-09-11 00:37:27
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements QuestionService
{
    @Resource
    private UserFacadeService userFacadeService;

    @Resource
    private QuestionMapper questionMapper;

    /**
     * 校验题目是否合法
     *
     * @param question
     * @param add
     */
    @Override
    public void validQuestion(Question question, boolean add)
    {
        if (question == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = question.getTitle();
        String content = question.getContent();
        String tags = question.getTags();
        String answer = question.getAnswer();
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();
        // 创建时，参数不能为空
        if (add)
        {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 8192)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }
        if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题用例过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题配置过长");
        }
    }

    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     *
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest)
    {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null)
        {
            return queryWrapper;
        }
        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tags = questionQueryRequest.getTags();
        String answer = questionQueryRequest.getAnswer();
        Long userId = questionQueryRequest.getUserId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
        if (!CollectionUtils.isEmpty(tags))
        {
            for (String tag : tags)
            {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request)
    {
        QuestionVO questionVO = QuestionVO.objToVo(question);
        // 1. 关联查询用户信息
        Long userId = question.getCreatorId();
        if (userId != null && userId > 0)
        {
            UserQueryFacadeRequest queryFacadeRequest = UserQueryFacadeRequest.build(
                    new UserIdQueryCondition(userId)
            );
            UserQueryResponse<UserVO> queryResponse = userFacadeService.query(queryFacadeRequest);
            questionVO.setUserVO(queryResponse.getData());

        }
        return questionVO;
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request)
    {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(),
                questionPage.getTotal());
        if (CollectionUtils.isEmpty(questionList))
        {
            return questionVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getCreatorId).collect(Collectors.toSet());

        UserQueryResponse<List<User>> listUserQueryResponse = userFacadeService.queryUserByIds(userIdSet);
        if (!listUserQueryResponse.isSucceed())
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "查询用户信息失败");
        }

        Map<Long, List<User>> userIdUserListMap = listUserQueryResponse
                .getData()
                .stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<QuestionVO> questionVOList = questionList.stream().map(question ->
        {
            QuestionVO questionVO = QuestionVO.objToVo(question);
            Long userId = question.getCreatorId();
            User user = null;
            if (userIdUserListMap.containsKey(userId))
            {
                user = userIdUserListMap.get(userId).get(0);
            }

            questionVO.setUserVO(UserConvertor.INSTANCE.convert(user));
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    @Override
    @Cached(name = "question", key = "#id", expire = 10, timeUnit = TimeUnit.MINUTES)
    public Question getQuestionById(Long id)
    {
        return questionMapper.selectById(id);
    }

    @Override
    @CacheUpdate(name = "question", key = "#question.id", value = "#question", condition = "#result == true")
    public boolean updateQuestion(Question question)
    {
        return questionMapper.updateById(question) > 0;
    }

    @Override
    public QuestionOperatorResponse<Boolean> doSave(Question question)
    {
        this.validQuestion(question, true);
        question.setFavourNum(0);
        question.setThumbNum(0);
        UserVO loginUser = getLoginUser();
        if (!isAdmin(loginUser))
        {
            return new QuestionOperatorResponse<>(ErrorCode.FORBIDDEN_ERROR, "只有管理员才能添加题目");
        }
        question.setCreatorId(loginUser.getId());
        return new QuestionOperatorResponse<>(questionMapper.insert(question) > 0);
    }

    @Override
    public QuestionOperatorResponse<Question> doUpdate(Question question)
    {
        // 参数校验
        validQuestion(question, false);
        long id = question.getId();
        if (!isAdmin(getLoginUser()))
        {
            return new QuestionOperatorResponse<>(ErrorCode.FORBIDDEN_ERROR, "只有管理员才能修改题目");
        }
        // 判断是否存在
        Question oldQuestion = getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = updateQuestion(question);
        if (result)
        {
            return new QuestionOperatorResponse<>(question);
        }
        else
        {
            return new QuestionOperatorResponse<>(ErrorCode.OPERATION_ERROR, "更新失败");
        }
    }

    @Override
    public QuestionOperatorResponse<Boolean> doDelete(Long id)
    {
        if (!isAdmin(getLoginUser()))
        {
            return new QuestionOperatorResponse<>(ErrorCode.FORBIDDEN_ERROR, "只有管理员才能删除题目");
        }
        return new QuestionOperatorResponse<>(questionMapper.deleteById(id) > 0);
    }

    @Override
    public QuestionQueryResponse<Page<Question>> getQuestionPage(Long current, Long size)
    {
        Page<Question> questionPage = this.page(new Page<>(current, size));
        return new QuestionQueryResponse<>(questionPage);
    }


    private UserVO getLoginUser()
    {
        Object loginId = StpUtil.getLoginId();
        return (UserVO) StpUtil.getSessionByLoginId(loginId).get((String) loginId);
    }

    private boolean isAdmin(UserVO userVO)
    {
        return userVO.getUserRole().equals(UserRoleEnum.ADMIN);
    }
}




