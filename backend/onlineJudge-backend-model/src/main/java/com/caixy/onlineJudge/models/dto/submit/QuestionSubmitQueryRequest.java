package com.caixy.onlineJudge.models.dto.submit;

import com.caixy.onlineJudge.models.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 题目提交查询请求
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.submit.QuestionSubmitQueryRequest
 * @since 2024/9/11 上午1:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable
{

    /**
     * 编程语言
     */
    private String language;

    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;


    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}