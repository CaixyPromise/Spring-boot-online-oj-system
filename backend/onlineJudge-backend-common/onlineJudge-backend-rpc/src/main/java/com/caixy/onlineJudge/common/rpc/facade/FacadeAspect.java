package com.caixy.onlineJudge.common.rpc.facade;

import com.alibaba.fastjson2.JSON;
import com.caixy.onlineJudge.common.base.utils.BeanValidator;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.common.response.ResultUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Facade的切面处理类，统一统计进行参数校验及异常捕获
 *
 * @author CAIXYPROMISE
 */
@Aspect
@Component
public class FacadeAspect
{

    private static final Logger LOGGER = LoggerFactory.getLogger(FacadeAspect.class);

    @Around("@annotation(com.caixy.onlineJudge.common.rpc.facade.Facade)")
    public Object facade(ProceedingJoinPoint pjp) throws Exception
    {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Object[] args = pjp.getArgs();
        LOGGER.info("start to execute , method = {} , args = {}", method.getName(), JSON.toJSONString(args));

//        Class<?> returnType = ((MethodSignature) pjp.getSignature()).getMethod().getReturnType();

        //循环遍历所有参数，进行参数校验
        try
        {
            // Validate parameters
            for (Object parameter : args)
            {
                BeanValidator.validateObject(parameter);
            }
            // Proceed with method execution
            Object result = pjp.proceed();
            printLog(stopWatch, method, args, "Execution completed", result, null);
            return result;
        }
        catch (ValidationException e)
        {
            LOGGER.error("Validation failed: {}", e.getMessage(), e);
            return handleException(stopWatch, method, args, e);
        }
        catch (Throwable e)
        {
            LOGGER.error("Execution failed: {}", e.getMessage(), e);
            return handleException(stopWatch, method, args, e);
        }
    }

    /**
     * 日志打印
     *
     * @param stopWatch
     * @param method
     * @param args
     * @param action
     * @param response
     */
    private void printLog(StopWatch stopWatch, Method method, Object[] args, String action, Object response,
                          Throwable throwable)
    {
        try
        {
            //因为此处有JSON.toJSONString，可能会有异常，需要进行捕获，避免影响主干流程
            LOGGER.info(getInfoMessage(action, stopWatch, method, args, response, throwable), throwable);
            // 如果校验失败，则返回一个失败的response
        }
        catch (Exception e1)
        {
            LOGGER.error("log failed", e1);
        }
    }

    /**
     * 统一格式输出，方便做日志统计
     * <p>
     * *** 如果调整此处的格式，需要同步调整日志监控 ***
     *
     * @param action    行为
     * @param stopWatch 耗时
     * @param method    方法
     * @param args      参数
     * @param response  响应
     * @return 拼接后的字符串
     */
    private String getInfoMessage(String action, StopWatch stopWatch, Method method, Object[] args, Object response,
                                  Throwable exception)
    {

        StringBuilder stringBuilder = new StringBuilder(action);
        stringBuilder.append(" ,method = ");
        stringBuilder.append(method.getName());
        stringBuilder.append(" ,cost = ");
        stringBuilder.append(stopWatch.getTime()).append(" ms");
        if (response instanceof BaseResponse)
        {
            stringBuilder.append(" ,code = ");
            stringBuilder.append(((BaseResponse) response).getCode());
        }
        if (exception != null)
        {
            stringBuilder.append(" ,success = ");
            stringBuilder.append(false);
        }
        stringBuilder.append(" ,args = ");
        stringBuilder.append(JSON.toJSONString(Arrays.toString(args)));

        if (response != null)
        {
            stringBuilder.append(" ,resp = ");
            stringBuilder.append(JSON.toJSONString(response));
        }

        if (exception != null)
        {
            stringBuilder.append(" ,exception = ");
            stringBuilder.append(exception.getMessage());
        }

        if (response instanceof BaseResponse)
        {
            BaseResponse baseResponse = (BaseResponse) response;
            if (!(baseResponse.getCode() == 0))
            {
                stringBuilder.append(" , execute_failed");
            }
        }

        return stringBuilder.toString();
    }

    /**
     * 定义并返回一个通用的失败响应
     */
    private BaseResponse<?> handleException(StopWatch stopWatch, Method method, Object[] args, Throwable throwable)
    {
        printLog(stopWatch, method, args, "Failed to execute", null, throwable);
        if (throwable instanceof BusinessException)
        {
            BusinessException be = (BusinessException) throwable;
            return ResultUtils.error(ErrorCode.getEnumByCode(be.getCode()));
        }
        else
        {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
        }
    }
}
