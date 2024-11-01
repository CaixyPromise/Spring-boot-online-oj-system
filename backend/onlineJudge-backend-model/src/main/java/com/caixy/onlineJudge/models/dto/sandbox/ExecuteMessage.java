package com.caixy.onlineJudge.models.dto.sandbox;

import lombok.Data;

import java.io.Serializable;

/**
 * 执行信息
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.sandbox.ExecuteMessage
 * @since 2024/9/11 上午2:11
 */
@Data
public class ExecuteMessage implements Serializable
{

    /**
     * 执行结果
     * 0-正常
     * 1（非零）-异常
     */
    private Integer exitValue;

    /**
     * 输入参数对应的输出结果 key: 输入参数 -> value: 输出参数
     */
    private ExecuteCase executeCase;
    /**
     * 执行时间
     */
    private Long time;

    /**
     * 内存占用
     */
    private Long memoryUsage;

    private static final long serialVersionUID = 1L;
}