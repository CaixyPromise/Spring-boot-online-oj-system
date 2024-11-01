package com.caixy.onlineJudge.models.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新题目
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.quetion.QuestionUpdateRequest
 * @since 2024/9/11 上午12:52
 */
@Data
public class QuestionUpdateRequest implements Serializable
{


    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

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
