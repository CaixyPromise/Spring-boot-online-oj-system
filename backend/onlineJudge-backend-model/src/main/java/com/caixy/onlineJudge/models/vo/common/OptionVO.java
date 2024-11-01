package com.caixy.onlineJudge.models.vo.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用的选项返回Vo
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.vo.common.OptionVO
 * @since 2024/9/13 下午5:15
 */
@Data
public class OptionVO<T> implements Serializable
{
    private String label;
    private T value;
    private static final long serialVersionUID = 1L;
}
