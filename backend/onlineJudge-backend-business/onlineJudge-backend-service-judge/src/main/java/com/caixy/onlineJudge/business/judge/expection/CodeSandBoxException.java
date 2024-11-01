package com.caixy.onlineJudge.business.judge.expection;

import com.caixy.onlineJudge.models.enums.sandbox.CodeSandBoxResultEnum;

/**
 * 代码沙箱执行错误
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.judge.expection.CodeSandBoxException
 * @since 2024/9/11 上午2:16
 */
public class CodeSandBoxException extends RuntimeException
{
    private final int code;

    public CodeSandBoxException(int code, String message)
    {
        super(message);
        this.code = code;
    }

    public CodeSandBoxException(CodeSandBoxResultEnum errorCode)
    {
        super(errorCode.getText());
        this.code = errorCode.getCode();
    }

    public CodeSandBoxException(CodeSandBoxResultEnum errorCode, String message)
    {
        super(message);
        this.code = errorCode.getCode();
    }
}
