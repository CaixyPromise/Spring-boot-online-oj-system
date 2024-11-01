package com.caixy.onlineJudge.models.dto.sandbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 执行代码请求
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.sandbox.ExecuteCodeRequest
 * @since 2024/9/11 上午1:24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeRequest implements Serializable
{
    private Long submitId;
    /**
     * 输入样例列表
     */
    private List<String> inputList;

    /**
     * 代码
     */
    private String code;

    /**
     * 语言
     */
    private String language;

    /**
     * 提交人id
     */
    private Long userId;
}
