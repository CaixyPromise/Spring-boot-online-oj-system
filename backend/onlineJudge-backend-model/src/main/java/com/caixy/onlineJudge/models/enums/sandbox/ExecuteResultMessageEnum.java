package com.caixy.onlineJudge.models.enums.sandbox;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 执行结果返回枚举
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.enums.sandbox.ExecuteResultMessageEnum
 * @since 2024/9/11 上午2:22
 */
@Getter
public enum ExecuteResultMessageEnum
{
    ACCEPTED("成功", "Accepted"),

    WRONG_ANSWER("答案错误", "Wrong Answer"),

    COMPILE_ERROR("Compile Error", "编译错误"),

    MEMORY_LIMIT_EXCEEDED("Out of Memory", "内存溢出"),

    TIME_LIMIT_EXCEEDED("Time Limit Exceeded", "超时"),

    PRESENTATION_ERROR("Presentation Error", "展示错误"),

    WAITING("Waiting", "等待中"),

    OUTPUT_LIMIT_EXCEEDED("Output Limit Exceeded", "输出溢出"),

    DANGEROUS_OPERATION("Dangerous Operation", "危险操作"),

    RUNTIME_ERROR("Runtime Error", "运行错误"),

    RUNTIME_FAILED("Runtime Failed", "运行失败"),

    SYSTEM_ERROR("System Error","系统错误");

    private final String text;

    private final String value;

    ExecuteResultMessageEnum(String text, String value)
    {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static ExecuteResultMessageEnum getEnumByValue(String value)
    {
        if (ObjectUtils.isEmpty(value))
        {
            return null;
        }
        for (ExecuteResultMessageEnum anEnum : ExecuteResultMessageEnum.values())
        {
            if (anEnum.value.equals(value))
            {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue()
    {
        return value;
    }

    public String getText()
    {
        return text;
    }
}
