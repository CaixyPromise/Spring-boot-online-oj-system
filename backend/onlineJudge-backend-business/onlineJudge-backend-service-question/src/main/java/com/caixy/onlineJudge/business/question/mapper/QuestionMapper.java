package com.caixy.onlineJudge.business.question.mapper;

import com.caixy.onlineJudge.models.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author CAIXYPROMISE
 * @description 针对表【question(题目)】的数据库操作Mapper
 * @createDate 2024-09-11 00:37:27
 * @Entity com.caixy.onlineJudge.models.entity.Question
 */
public interface QuestionMapper extends BaseMapper<Question>
{
    Question selectQuestionById(Long id);
}




