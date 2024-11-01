package com.caixy.onlineJudge.business.question.mapper;

import com.caixy.onlineJudge.models.entity.QuestionSubmit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author CAIXYPROMISE
 * @description 针对表【question_submit(题目提交)】的数据库操作Mapper
 * @createDate 2024-09-11 00:37:27
 * @Entity com.caixy.onlineJudge.models.entity.QuestionSubmit
 */
public interface QuestionSubmitMapper extends BaseMapper<QuestionSubmit>
{
    QuestionSubmit selectBySubmitId(Long submitId);

}




