package com.caixy.onlineJudge.business.judge.manager.core;

import com.caixy.onlineJudge.models.dto.sandbox.JudgeInfo;

/**
 * 判题策略接口
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.judge.manager.core.JudgeStrategy
 * @since 2024/9/11 下午5:12
 */
public interface JudgeStrategy
{
    JudgeInfo doJudge(JudgeContext judgeContext);
}
