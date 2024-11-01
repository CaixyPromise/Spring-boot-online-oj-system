package com.caixy.onlineJudge.business.judge.manager;

import com.caixy.onlineJudge.business.judge.annotation.LangProvider;
import com.caixy.onlineJudge.business.judge.manager.core.JudgeContext;
import com.caixy.onlineJudge.business.judge.manager.core.JudgeStrategy;
import com.caixy.onlineJudge.business.judge.manager.impl.JavaStrategyImpl;
import com.caixy.onlineJudge.common.base.utils.SpringContextUtils;
import com.caixy.onlineJudge.models.dto.sandbox.JudgeInfo;
import com.caixy.onlineJudge.models.enums.sandbox.LanguageProviderEnum;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 判题服务管理器
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.judge.manager.JudgeManager
 * @since 2024/9/11 下午5:11
 */
@Service
public class JudgeManager
{
    @Resource
    private List<JudgeStrategy> judgeStrategyList;

    private ConcurrentHashMap<LanguageProviderEnum, JudgeStrategy> judgeStrategyMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init()
    {
        judgeStrategyMap = SpringContextUtils.getServiceFromAnnotation(judgeStrategyList, LangProvider.class, "value");
    }

    private JudgeStrategy getInstance(LanguageProviderEnum providerEnum)
    {
        JudgeStrategy judgeStrategy = judgeStrategyMap.get(providerEnum);
        if (judgeStrategy == null)
        {
            throw new RuntimeException("未找到对应的判题策略");
        }
        return judgeStrategy;
    }

    public JudgeInfo doJudge(LanguageProviderEnum providerEnum, JudgeContext judgeContext)
    {
        JudgeStrategy instance = getInstance(providerEnum);
        return instance.doJudge(judgeContext);
    }
}
