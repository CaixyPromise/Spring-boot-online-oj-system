package com.caixy.onlineJudge.models.enums.sandbox;

import lombok.Getter;

/**
 * 沙箱执行错误码
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.enums.sandbox.CodeSandBoxCodeEnum
 * @since 2024/9/11 上午2:17
 */
@Getter
public enum CodeSandBoxResultEnum
{
    /**
     * 错误的语言
     */
    LANGUAGE_ERROR(10001, "错误的语言"),
    /**
     * 危险操作
     */
    FORBIDDEN_CODE_ERROR(10002, "含有违禁代码"),
    /**
     * 运行中发生错误
     */
    RUNTIME_ERROR(10003, "运行中发生错误"),

    /**
     * 非0返回
     */
    NON_ZERO_RETURN_CODE(10005, "非0返回"),
    /**
     * 编译错误
     */
    COMPILE_ERROR(10004, "编译错误"),
    /**
     * 系统错误
     */
    SYSTEM_ERROR(-99999, "系统错误"),
    /**
     * 成功
     */
    SUCCESS(0, "运行成功"),

    /**
     * 失败
     */
    FAILED(-1, "运行失败"),

    OPERATION_ERROR(-99998, "操作失败"),

    /**
     * 无权限
     */
    AUTHENTICATION_ERROR(-2, "无权限");


    private final String text;
    private final int code;

    CodeSandBoxResultEnum(int code, String text)
    {
        this.code = code;
        this.text = text;
    }
}
