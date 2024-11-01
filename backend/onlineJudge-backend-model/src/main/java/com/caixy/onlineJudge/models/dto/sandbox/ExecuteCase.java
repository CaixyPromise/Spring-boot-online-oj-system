package com.caixy.onlineJudge.models.dto.sandbox;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 执行样例输入与输出
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.sandbox.ExcuateCase
 * @since 2024/9/11 上午2:09
 */
@Data
@AllArgsConstructor
public class ExecuteCase implements Serializable
{
    //  输入参数
    private String inputArgs;
    //  输出结果
    private String outputValue;

    private static final long serialVersionUID = 1L;
}
