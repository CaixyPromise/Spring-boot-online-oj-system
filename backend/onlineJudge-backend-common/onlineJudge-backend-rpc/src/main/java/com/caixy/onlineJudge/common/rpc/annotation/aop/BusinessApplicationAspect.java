package com.caixy.onlineJudge.common.rpc.annotation.aop;

import com.caixy.onlineJudge.common.rpc.annotation.BusinessApplication;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 业务自定义注解切面方法
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.common.rpc.annotation.aop.BusinessApplicationAspect
 * @since 2024/7/31 下午4:29
 */
@Aspect
@Component
public class BusinessApplicationAspect
{

    @Before("execution(* org.springframework.boot.SpringApplication.run(..)) && args(primarySource,..)")
    public void beforeRun(Class<?> primarySource)
    {
        BusinessApplication annotation = AnnotationUtils.findAnnotation(primarySource, BusinessApplication.class);
        if (annotation != null)
        {
            String[] defaultPackages = {"com.caixy.onlineJudge.common", "com.caixy.onlineJudge.web"};
            String[] customPackages = annotation.scanBasePackages();
            String[] mergedPackages = mergeArrays(defaultPackages, customPackages);

            System.setProperty("spring.scan.base.packages", String.join(",", mergedPackages));

            Class<?>[] defaultExclude = {};
            Class<?>[] customExclude = annotation.exclude();
            Class<?>[] mergedExclude = mergeArrays(defaultExclude, customExclude);

            System.setProperty("spring.autoconfigure.exclude", joinClassNames(mergedExclude));
        }
    }

    private String[] mergeArrays(String[] defaultArray, String[] customArray)
    {
        Set<String> set = new HashSet<>(Arrays.asList(defaultArray));
        set.addAll(Arrays.asList(customArray));
        return set.toArray(new String[0]);
    }

    private Class<?>[] mergeArrays(Class<?>[] defaultArray, Class<?>[] customArray)
    {
        Set<Class<?>> set = new HashSet<>(Arrays.asList(defaultArray));
        set.addAll(Arrays.asList(customArray));
        return set.toArray(new Class<?>[0]);
    }

    private String joinClassNames(Class<?>[] classes)
    {
        String[] classNames = new String[classes.length];
        for (int i = 0; i < classes.length; i++)
        {
            classNames[i] = classes[i].getName();
        }
        return String.join(",", classNames);
    }
}