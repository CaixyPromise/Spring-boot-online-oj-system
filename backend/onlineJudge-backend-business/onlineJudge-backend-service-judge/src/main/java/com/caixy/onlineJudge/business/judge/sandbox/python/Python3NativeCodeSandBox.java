package com.caixy.onlineJudge.business.judge.sandbox.python;

import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.caixy.onlineJudge.business.judge.annotation.LangProvider;
import com.caixy.onlineJudge.business.judge.expection.CodeSandBoxException;
import com.caixy.onlineJudge.business.judge.sandbox.CodeSandBox;
import com.caixy.onlineJudge.business.judge.sandbox.DefaultCodeSandBoxTemplate;
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
 * Python3 原生代码沙箱实现
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.judge.sandbox.python.Python3NativeCodeSandBox
 * @since 2024/9/11 上午2:31
 */
@Service
@Slf4j
@LangProvider(LanguageProviderEnum.PYTHON_3)
public class Python3NativeCodeSandBox extends DefaultCodeSandBoxTemplate implements CodeSandBox
{

    /**
     * 代码黑名单字典树
     */
    private static final WordTree WORD_TREE;
    private static final String CMD_PREFIX;

    static
    {
        // 初始化黑名单字典树
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(Python3SandBoxConstant.BLACK_LIST);
        CMD_PREFIX
                = System.getProperty("os.name").toLowerCase().contains("win")
                  ? "python"
                  : "python3";
    }

    @Override
    public LanguageProviderEnum getLangProviderEnum()
    {
        return LanguageProviderEnum.PYTHON_3;
    }

    /**
     * 代码运行前的预处理，处理完后运行代码
     * 操作：
     * <li>1. 检测代码是否包含黑名单词汇</li>
     * <li>2. 保存代码到文件中</li>
     * <li>3. 运行代码</li>
     * <li>4. 收集运行结果</li>
     * <li>5. 删除代码文件</li>
     *
     * @return 代码文件信息
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/2/2 20:25
     */
    @Override
    public File preExecuteCode(ExecuteCodeRequest executeCodeRequest)
    {
        // 2. 获取代码
        String code = executeCodeRequest.getCode();
        // 3. 获取语言
        String language = executeCodeRequest.getLanguage();

        // 4. 检测代码是否包含黑名单词汇
        FoundWord foundWord = WORD_TREE.matchWord(code);
        if (foundWord != null)
        {
            throw new CodeSandBoxException(CodeSandBoxResultEnum.FORBIDDEN_CODE_ERROR);
        }

        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("nix") || osName.contains("nux"))
        {
            code = Python3SandBoxConstant.MEMORY_LIMIT_PREFIX_CODE + "\r\n" + code;
            System.out.println("安全控制后的代码：\n" + code);
        }
        // 5. 保存代码
        return dumpsCodeToFile(code,
                language,
                Python3SandBoxConstant.GLOBAL_PYTHON_FILE_NAME,
                executeCodeRequest.getUserId(),
                executeCodeRequest.getSubmitId());
//        // 6. 执行代码
//        List<ExecuteMessage> executeMessageList = runCode(userCodeFile, inputList);

    }

    /**
     * 执行代码
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/11 上午2:47
     */
    @Override
    public List<ExecuteMessage> executeCode(File file, List<String> inputList)
    {
        // 1. 获取文件代码路径
        String fileAbsolutePath = file.getAbsolutePath();
        // 2. 初始化结果输出链表，长度为样例输入个数
        List<ExecuteMessage> executeMessageList = new ArrayList<>(inputList.size());
        for (String inputArg : inputList)
        {
            // 3. 初始化执行命令
            String runCmd = String.format("%s %s.py %s", CMD_PREFIX, fileAbsolutePath, inputArg);
            // 4. 运行程序
            ExecuteMessage result = this.getRuntime(runCmd, file.getName(),
                    inputArg,
                    Python3SandBoxConstant.TIME_OUT);
            executeMessageList.add(result);
            // 如果程序运行结果不是0，则直接返回
            if (result.getExitValue() != 0)
            {
                return executeMessageList;
            }
        }
        return executeMessageList;
    }

    @Override
    public ExecuteCodeResponse afterExecuteCode(List<ExecuteMessage> executeMessageList, File file)
    {
        // 7. 收集运行结果
        ExecuteCodeResponse response = judgeOutputAndMakeResponse(executeMessageList);
        // 8. 删除代码文件
        boolean deleteResult = deleteCodeFile(file);
        // 9. 删除代码文件失败处理
        if (!deleteResult)
        {
            log.error("删除代码文件失败，文件路径：{}", file.getAbsolutePath());
        }
        return response;
    }
}
