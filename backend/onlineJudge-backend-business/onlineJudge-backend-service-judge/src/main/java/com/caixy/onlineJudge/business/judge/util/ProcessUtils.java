package com.caixy.onlineJudge.business.judge.util;

import com.caixy.onlineJudge.models.dto.sandbox.ExecuteMessage;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 进程工具类
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.judge.util.ProcessUtils
 * @since 2024/9/11 上午2:07
 */
public class ProcessUtils
{
    public static ExecuteMessage getProcessInfo(Process process)
    {
        ExecuteMessage message = new ExecuteMessage();
        try
        {
            // 如果进程还在运行，就等待结束返回
            while (process.isAlive())
            {
                process.waitFor();
            }
            int exitCode = process.exitValue();
            message.setExitValue(exitCode);
            if (exitCode == 0)
            {
                // 获取进程输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                List<String> outputStrList = new ArrayList<>();
                // 按行来获取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null)
                {
                    outputStrList.add(compileOutputLine);
                }
                message.getExecuteCase().setOutputValue(StringUtils.join(outputStrList, '\n'));
            }
            else
            {
                // 获取进程错误输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                List<String> errorStrList = new ArrayList<>();
                // 按行来获取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null)
                {
                    errorStrList.add(compileOutputLine);
                }
                message.getExecuteCase().setOutputValue(StringUtils.join(errorStrList, '\n'));
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return message;
    }

    public static Long getRuntimeMemory()
    {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
