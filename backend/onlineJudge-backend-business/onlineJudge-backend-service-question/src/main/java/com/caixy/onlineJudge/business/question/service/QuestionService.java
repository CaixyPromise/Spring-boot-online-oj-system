package com.caixy.onlineJudge.business.question.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.onlineJudge.models.dto.question.QuestionQueryRequest;
import com.caixy.onlineJudge.models.entity.Question;
import com.caixy.onlineJudge.models.vo.quetion.QuestionVO;
import com.caixy.serviceclient.service.question.response.QuestionOperatorResponse;
import com.caixy.serviceclient.service.question.response.QuestionQueryResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CAIXYPROMISE
 * @description 针对表【question(题目)】的数据库操作Service
 * @createDate 2024-09-11 00:37:27
 */
public interface QuestionService extends IService<Question>
{
    void validQuestion(Question question, boolean add);

    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

    Question getQuestionById(Long id);

    boolean updateQuestion(Question question);

    QuestionOperatorResponse<Boolean> doSave(Question question);

    QuestionOperatorResponse<Question> doUpdate(Question question);

    QuestionOperatorResponse<Boolean> doDelete(Long id);

    QuestionQueryResponse<Page<Question>> getQuestionPage(Long current, Long size);
}
