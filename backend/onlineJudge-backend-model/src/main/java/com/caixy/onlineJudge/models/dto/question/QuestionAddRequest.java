package com.caixy.onlineJudge.models.dto.question;

import com.caixy.onlineJudge.models.enums.question.QuestionDifficultyEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建题目
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.quetion.QuestionAddRequest
 * @since 2024/9/11 上午12:52
 */
@Data
public class QuestionAddRequest implements Serializable
{
    /**
     * 标题
     */
    private String title;

    /**
     * 题目难度
     */
    private QuestionDifficultyEnum difficulty;


    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;

    private static final long serialVersionUID = 1L;
}
