package com.caixy.onlineJudge.common.response;


import com.caixy.onlineJudge.constants.code.ErrorCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 */
@Data
@NoArgsConstructor
public class BaseResponse<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int code;

    private T data;

    private String message;


    public Boolean isSucceed()
    {
        return code == 0;
    }

    public BaseResponse(int code, T data, String message)
    {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(ErrorCode errorCode, T data)
    {
        this.code = errorCode.getCode();
        this.data = data;
        this.message = errorCode.getMessage();
    }


    public BaseResponse(int code, T data)
    {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode)
    {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }

    public BaseResponse(ErrorCode errorCode, String message)
    {
        this(errorCode.getCode(), null, message);
    }


}
