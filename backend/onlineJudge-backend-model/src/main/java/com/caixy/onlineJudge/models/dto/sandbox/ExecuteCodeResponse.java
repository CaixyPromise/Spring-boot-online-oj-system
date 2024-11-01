package com.caixy.onlineJudge.models.dto.sandbox;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 代码执行结果返回
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.sandbox.ExecuteCodeResponse
 * @since 2024/9/11 上午1:25
 */
@Data
public class ExecuteCodeResponse
{
    private List<String> outputList;

    /**
     * 接口信息
     */
    private String message;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;

}
