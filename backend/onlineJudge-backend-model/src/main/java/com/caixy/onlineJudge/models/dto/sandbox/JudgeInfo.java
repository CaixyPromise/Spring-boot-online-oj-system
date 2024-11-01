package com.caixy.onlineJudge.models.dto.sandbox;

import lombok.Data;

/**
 * 判题信息
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.sandbox.JudgeInfo
 * @since 2024/9/11 上午1:25
 */
@Data
public class JudgeInfo {

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 消耗内存
     */
    private Long memory;

    /**
     * 消耗时间（KB）
     */
    private Long time;

    public static JudgeInfo build(String outputValue, long maxMemoryUsage, long maxTime)
    {
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(outputValue);
        judgeInfo.setMemory(maxMemoryUsage);
        judgeInfo.setTime(maxTime);
        return judgeInfo;
    }
}
