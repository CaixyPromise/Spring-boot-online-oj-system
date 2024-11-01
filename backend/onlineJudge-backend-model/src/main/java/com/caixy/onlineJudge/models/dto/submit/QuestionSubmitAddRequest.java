package com.caixy.onlineJudge.models.dto.submit;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目提交请求类
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.submit.QuestionSubmitAddRequest
 * @since 2024/9/11 上午1:22
 */
@Data
public class QuestionSubmitAddRequest implements Serializable
{

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}