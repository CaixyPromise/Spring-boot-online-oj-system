package com.caixy.onlineJudge.business.question.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.onlineJudge.models.dto.submit.QuestionSubmitAddRequest;
import com.caixy.onlineJudge.models.dto.submit.QuestionSubmitQueryRequest;
import com.caixy.onlineJudge.models.entity.QuestionSubmit;
import com.caixy.onlineJudge.models.vo.submit.QuestionSubmitVO;
import com.caixy.onlineJudge.models.vo.user.UserVO;

/**
 * @author CAIXYPROMISE
 * @description 针对表【question_submit(题目提交)】的数据库操作Service
 * @createDate 2024-09-11 00:37:27
 */
public interface QuestionSubmitService extends IService<QuestionSubmit>
{

    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, UserVO loginUser);

    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, UserVO loginUser);

    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, UserVO loginUser);

    QuestionSubmit getQuestionSubmitById(Long id);

    Boolean updateQuestionSubmit(QuestionSubmit questionSubmit);
}
