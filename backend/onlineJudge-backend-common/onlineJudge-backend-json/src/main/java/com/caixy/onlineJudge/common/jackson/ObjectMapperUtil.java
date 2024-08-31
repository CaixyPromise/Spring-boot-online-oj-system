package com.caixy.onlineJudge.common.jackson;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@FunctionalInterface
interface CheckedSupplier<T>
{
    T get() throws Exception;
}

/**
 * 对象Mapper工具类
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.common.jackson.ObjectUtil
 * @since 2024/8/28 下午4:48
 */
public class ObjectMapperUtil
{
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static <T> T baseHandler(CheckedSupplier<T> supplier)
    {
        try
        {
            return supplier.get();
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("JSON processing error: " + e.getMessage(), e);
        }
        catch (Exception e)
        {
            throw new RuntimeException("General error: " + e.getMessage(), e);
        }
    }

    public static <T> T readValue(Object source, Class<T> valueType)
    {
        return baseHandler(() -> objectMapper.readValue(source.toString(), valueType));
    }

    public static <T> T readValue(byte[] source, Class<T> valueType)
    {
        return baseHandler(() -> objectMapper.readValue(source, valueType));
    }

    public static <T> T convertValue(Object source, Class<T> valueType)
    {
        return baseHandler(() -> objectMapper.convertValue(source, valueType));
    }
}