package com.caixy.onlineJudge.business.judge.factory;

import com.caixy.onlineJudge.business.judge.annotation.LangProvider;
import com.caixy.onlineJudge.business.judge.expection.CodeSandBoxException;
import com.caixy.onlineJudge.business.judge.sandbox.CodeSandBox;
import com.caixy.onlineJudge.common.base.utils.SpringContextUtils;
import com.caixy.onlineJudge.models.dto.sandbox.ExecuteCodeRequest;
import com.caixy.onlineJudge.models.dto.sandbox.ExecuteCodeResponse;
import com.caixy.onlineJudge.models.dto.sandbox.ExecuteMessage;
import com.caixy.onlineJudge.models.enums.sandbox.CodeSandBoxResultEnum;
import com.caixy.onlineJudge.models.enums.sandbox.LanguageProviderEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代码沙箱工厂
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.judge.factory.SandBoxProviderFactory
 * @since 2024/9/11 上午2:54
 */
@Slf4j
@Service
public class SandBoxProviderFactory
{
    @Resource
    private List<CodeSandBox> codeSandBox;
    private ConcurrentHashMap<LanguageProviderEnum, CodeSandBox> sandBoxMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init()
    {
        sandBoxMap = SpringContextUtils.getServiceFromAnnotation(codeSandBox, LangProvider.class, "value");
    }

    public CodeSandBox getInstance(LanguageProviderEnum languageProviderEnum)
    {
        return sandBoxMap.get(languageProviderEnum);
    }

    /**
     * 执行代码
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/11 上午3:01
     */
    public ExecuteCodeResponse doExecute(ExecuteCodeRequest executeCodeRequest,
                                         CodeSandBox sandBox)
    {
        if (sandBox == null)
        {
            throw new CodeSandBoxException(CodeSandBoxResultEnum.LANGUAGE_ERROR, "未找到对应的沙箱");
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 执行前处理，例如编译，代码检查
        File codeFile = sandBox.preExecuteCode(executeCodeRequest);
        // 执行代码
        List<ExecuteMessage> executeMessages = sandBox.executeCode(codeFile, executeCodeRequest.getInputList());
        // 执行完代码后处理，例如删除临时文件
        ExecuteCodeResponse executeCodeResponse = sandBox.afterExecuteCode(executeMessages, codeFile);
        stopWatch.stop();
        log.info("提交id: {}, 执行代码耗时: {}, 语言: {}",
                executeCodeRequest.getSubmitId(),
                stopWatch.getTotalTimeMillis(),
                sandBox.getLangProviderEnum());
        return executeCodeResponse;
    }

}
