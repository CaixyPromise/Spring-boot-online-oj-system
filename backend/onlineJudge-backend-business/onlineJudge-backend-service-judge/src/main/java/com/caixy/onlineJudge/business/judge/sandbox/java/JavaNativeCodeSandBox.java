package com.caixy.onlineJudge.business.judge.sandbox.java;

import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.caixy.onlineJudge.business.judge.annotation.LangProvider;
import com.caixy.onlineJudge.business.judge.expection.CodeSandBoxException;
import com.caixy.onlineJudge.business.judge.sandbox.CodeSandBox;
import com.caixy.onlineJudge.business.judge.sandbox.DefaultCodeSandBoxTemplate;
import com.caixy.onlineJudge.business.judge.util.ProcessUtils;
import com.caixy.onlineJudge.models.dto.sandbox.ExecuteCodeRequest;
import com.caixy.onlineJudge.models.dto.sandbox.ExecuteCodeResponse;
import com.caixy.onlineJudge.models.dto.sandbox.ExecuteMessage;
import com.caixy.onlineJudge.models.enums.sandbox.CodeSandBoxResultEnum;
import com.caixy.onlineJudge.models.enums.sandbox.LanguageProviderEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 沙箱实现
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.judge.sandbox.java.JavaNativeCodeSandBox
 * @since 2024/9/11 上午2:26
 */
@Service
@Slf4j
@LangProvider(LanguageProviderEnum.JAVA)
public class JavaNativeCodeSandBox extends DefaultCodeSandBoxTemplate implements CodeSandBox
{
    /**
     * 代码黑名单字典树
     */
    private static final WordTree WORD_TREE;
    private static final String RUNCODE_CMD_PREFIX =
            "java -Xmx128m -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=%s Main %s";

    private static final String COMPILER_CMD_PREFIX = "javac -encoding utf-8 %s";

    static
    {
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(JavaSandBoxConstant.BLACK_LIST);

    }

    @Override
    public LanguageProviderEnum getLangProviderEnum()
    {
        return LanguageProviderEnum.JAVA;
    }

    @Override
    public File preExecuteCode(ExecuteCodeRequest executeCodeRequest)
    {
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        log.info("当前操作系统：{}", System.getProperty("os.name").toLowerCase());
        log.info("当前代码使用语言：{}", language);
        // 限制敏感代码：黑名单检测
        FoundWord foundWord = WORD_TREE.matchWord(code);
        if (foundWord != null)
        {
            throw new CodeSandBoxException(CodeSandBoxResultEnum.FORBIDDEN_CODE_ERROR);
        }
        // 5. 保存代码
        return dumpsCodeToFile(code,
                language,
                JavaSandBoxConstant.GLOBAL_JAVA_CLASS_NAME,
                executeCodeRequest.getUserId(),
                executeCodeRequest.getSubmitId());
    }

    @Override
    public List<ExecuteMessage> executeCode(File file, List<String> inputList)
    {
        ExecuteMessage compileResult = compileCode(file);
        if (compileResult.getExitValue() != 0)
        {
            throw new CodeSandBoxException(CodeSandBoxResultEnum.COMPILE_ERROR);
        }
        // 7. 执行代码
        return runCode(file, inputList);
    }

    @Override
    public ExecuteCodeResponse afterExecuteCode(List<ExecuteMessage> executeMessageList, File file)
    {
        // 8. 收集运行结果
        ExecuteCodeResponse response = judgeOutputAndMakeResponse(executeMessageList);
        // 9. 删除代码文件
        boolean deleteResult = deleteCodeFile(file);
        // 10. 删除代码文件失败处理
        if (!deleteResult)
        {
            log.error("删除代码文件失败，文件路径：{}", file.getAbsolutePath());
        }
        return response;
    }

    /**
     * 编译代码
     *
     * @param userCodeFile
     * @return
     */
    public ExecuteMessage compileCode(File userCodeFile)
    {
        String compileCmd = String.format(COMPILER_CMD_PREFIX, userCodeFile.getAbsolutePath());
        try
        {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.getProcessInfo(compileProcess);
            // 编译失败
            if (executeMessage.getExitValue() != 0)
            {
                executeMessage.setExitValue(1);
                executeMessage.getExecuteCase().setOutputValue(CodeSandBoxResultEnum.COMPILE_ERROR.getText());
            }
            return executeMessage;
        }
        catch (Exception e)
        {
            // 未知错误
            ExecuteMessage executeMessage = new ExecuteMessage();
            executeMessage.setExitValue(CodeSandBoxResultEnum.COMPILE_ERROR.getCode());
            executeMessage.getExecuteCase().setOutputValue(CodeSandBoxResultEnum.COMPILE_ERROR.getText());
            return executeMessage;
        }
    }

    /**
     * 执行文件，获得执行结果列表
     *
     * @param userCodeFile
     * @param inputList
     * @return
     */
    public List<ExecuteMessage> runCode(File userCodeFile, List<String> inputList)
    {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();

        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList)
        {
            String runCMD = String.format(RUNCODE_CMD_PREFIX,
                    userCodeParentPath,
                    JavaSandBoxConstant.SECURITY_MANAGER_PATH,
                    JavaSandBoxConstant.SECURITY_MANAGER_CLASS_NAME,
                    inputArgs);
            // 运行程序
            ExecuteMessage result = this.getRuntime(
                    runCMD,
                    userCodeFile.getName(),
                    inputArgs,
                    JavaSandBoxConstant.TIME_OUT);
            executeMessageList.add(result);
            // 如果程序运行结果不是0，则直接返回
            if (result.getExitValue() != 0)
            {
                return executeMessageList;
            }
        }
        return executeMessageList;
    }
}
