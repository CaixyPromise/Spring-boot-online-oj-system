package com.caixy.onlineJudge.models.dto.question;

import lombok.Data;

/**
 * 题目配置
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.quetion.JudgeConfig
 * @since 2024/9/11 上午12:51
 */
@Data
public class JudgeConfig
{
    /**
     * 时间限制（ms）
     */
    private Long timeLimit;

    /**
     * 内存限制（KB）
     */
    private Long memoryLimit;

    /**
     * 堆栈限制（KB）
     */
    private Long stackLimit;
}
