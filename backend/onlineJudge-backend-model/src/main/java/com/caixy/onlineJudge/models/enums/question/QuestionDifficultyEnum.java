package com.caixy.onlineJudge.models.enums.question;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 题目难度枚举
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.enums.question.QuestionDifficultyEnum
 * @since 2024/9/12 下午4:33
 */
@Getter
@AllArgsConstructor
public enum QuestionDifficultyEnum
{
    EASY("简单", 1),
    MEDIUM("中等", 2),
    HARD("困难", 3);

    private final String name;
    private final Integer value;

    public static QuestionDifficultyEnum getEnumByValue(Integer value)
    {
        if (value == null)
        {
            return null;
        }
        for (QuestionDifficultyEnum questionDifficultyEnum : QuestionDifficultyEnum.values())
        {
            if (Objects.equals(questionDifficultyEnum.getValue(), value))
            {
                return questionDifficultyEnum;
            }
        }
        return null;
    }
}
