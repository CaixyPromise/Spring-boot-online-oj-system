package com.caixy.onlineJudge.business.judge.manager.core;


import com.caixy.onlineJudge.models.dto.question.JudgeCase;
import com.caixy.onlineJudge.models.dto.sandbox.JudgeInfo;
import com.caixy.onlineJudge.models.entity.Question;
import com.caixy.onlineJudge.models.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext
{
    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;
}
