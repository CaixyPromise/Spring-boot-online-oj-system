package com.caixy.onlineJudge.models.dto.question;

import lombok.Data;

/**
 * 判题用例
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.dto.quetion.JudgeCase
 * @since 2024/9/11 上午12:51
 */
@Data
public class JudgeCase {

    /**
     * 输入用例
     */
    private String input;

    /**
     * 输出用例
     */
    private String output;
}
