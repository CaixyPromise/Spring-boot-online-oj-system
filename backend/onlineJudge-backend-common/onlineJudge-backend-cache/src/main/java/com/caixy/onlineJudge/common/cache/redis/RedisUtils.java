package com.caixy.onlineJudge.common.cache.redis;

import com.caixy.onlineJudge.models.enums.redis.RedisKeyEnum;
import com.caixy.onlineJudge.common.json.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存操作类
 *
 * @name: com.caixy.project.utils.RedisUtils
 * @author: CAIXYPROMISE
 * @since: 2023-12-20 20:14
 **/
@Component
@AllArgsConstructor
@Slf4j
public class RedisUtils
{
    // 调用接口排名信息的最大容量
    private static final Long REDIS_INVOKE_RANK_MAX_SIZE = 10L;

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据枚举获取Key，并且根据字段值生成完整的Key值，自动拼接冒号
     *
     * @author CAIXYPROMISE
     * @version 2.0
     * @since 2024/2/16 21:02
     */
    private String getFullKey(RedisKeyEnum keyEnum, Object itemName)
    {
        // 使用StringBuilder来构建完整的Key
        StringBuilder fullKey = new StringBuilder(keyEnum.getKey());

        // 确保Key以冒号结尾
        if (fullKey.charAt(fullKey.length() - 1) != ':')
        {
            fullKey.append(':');
        }

        // 如果itemName不为空，追加到Key后面
        if (itemName != null)
        {
            fullKey.append(itemName);
        }

        return fullKey.toString();
    }

    /**
     * 删除Key的数据
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2023/12/10 20:19
     */
    public boolean delete(String key)
    {
        return Boolean.TRUE.equals(stringRedisTemplate.delete(key));
    }


    /**
     * 删除Key的数据：接受来自常量的配置
     *
     * @author CAIXYPROMISE
     * @version 2.0
     * @since 2024/2/16 20:19
     */
    public boolean delete(RedisKeyEnum Enum, String... keyItems)
    {
        return Boolean.TRUE.equals(stringRedisTemplate.delete(Enum.generateKey(keyItems)));
    }

    /**
     * 刷新过期时间：接受来自常量的配置
     *
     * @author CAIXYPROMISE
     * @version 2.0
     * @since 2024/2/16 20:19
     */
    public void refreshExpire(RedisKeyEnum Enum, long expire, String... keyItems)
    {
        refreshExpire(Enum.generateKey(keyItems), expire);
    }

    /**
     * 刷新过期时间
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2023/1220 20:18
     */
    public void refreshExpire(String key, long expire)
    {
        stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 获取字符串数据：接受来自常量的配置
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2023/1220 20:18
     */
    public String getString(RedisKeyEnum Enum, String... keyItems)
    {
        return stringRedisTemplate.opsForValue().get(Enum.generateKey(keyItems));
    }

    /**
     * 获取字符串数据
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2023/1220 20:18
     */
    public String getString(String key)
    {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public <JsonType> List<JsonType> getJson(RedisKeyEnum keyEnum, String... keyItems)
    {
        String cacheData = stringRedisTemplate.opsForValue().get(keyEnum.generateKey(keyItems));
        if (StringUtils.isNotBlank(cacheData))
        {
            // 把字符串转义全部清楚掉
            return JsonUtils.toList(cacheData);
        }
        // 直接返回空指针，不返回空列表
        return null;
    }

    public <JsonType> JsonType getJson(RedisKeyEnum keyEnum, Class<JsonType> returnType, String... keyItems)
    {
        String cacheData = stringRedisTemplate.opsForValue().get(keyEnum.generateKey(keyItems));
        if (StringUtils.isNotBlank(cacheData))
        {
            return JsonUtils.jsonToObject(cacheData, returnType);
        }
        return null;
    }

    /**
     * 放入一个对象数据
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/6/16 上午10:32
     */
    public void setObject(RedisKeyEnum keyEnum, Object value, String... keyItems)
    {
        setString(keyEnum, JsonUtils.toJson(value), keyItems);
    }

    /**
     * 从redis获取对象，因为放入时已经处理序列化，所以在这里需要从json转对象
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/7/2 下午9:18
     */
    public <T> T getObject(RedisKeyEnum keyEnum, Class<T> returnType, String... keyItems)
    {
        return getJson(keyEnum, returnType, keyItems);
    }


    /**
     * 获取哈希数据：接受来自常量的配置
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2023/1220 20:18
     */
    public Map<Object, Object> getHashMap(RedisKeyEnum Enum, String... keyItems)
    {
        return stringRedisTemplate.opsForHash().entries(Enum.generateKey(keyItems));
    }


    /**
     * 获取hash数据，接受常量配置，并且根据类型回传对应类型的HashMap
     *
     * @param enumKey   key常量
     * @param keyItems  key名称
     * @param keyType   key类型
     * @param valueType value类型
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/2/24 00:16
     */
    public <K, V> Map<K, V> getHashMap(RedisKeyEnum keyEnum, Class<K> keyType, Class<V> valueType, String... keyItems)
    {
        String fullKey = keyEnum.generateKey(keyItems);
        Map<Object, Object> rawMap = redisTemplate.opsForHash().entries(fullKey);
        if (rawMap.isEmpty())
        {
            return Collections.emptyMap();
        }
        Map<K, V> typedMap = new HashMap<>();
        rawMap.forEach((rawKey, rawValue) ->
        {
            K key = keyType.cast(rawKey);
            V value = valueType.cast(rawValue);
            typedMap.put(key, value);
        });
        return typedMap;
    }

    /**
     * 放入hash类型的数据 - Hash<String, Object> 接受来自常量的配置
     *
     * @param data 数据
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2023/12/20 2:16
     */
    public <K, V> void setHashMap(RedisKeyEnum keyEnum, Map<K, V> data, String... keyItems)
    {
        String fullKey = keyEnum.generateKey(keyItems);
        redisTemplate.opsForHash().putAll(fullKey, data);
        Long expire = keyEnum.getExpire();
        if (expire != null)
        {
            redisTemplate.expire(fullKey, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * 放入hash类型的数据 - Hash<String, Object>
     *
     * @param key    redis-key
     * @param data   数据
     * @param expire 过期时间, 单位: 秒
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2023/12/20 2:16
     */
    public void setHashMap(String key, HashMap<String, Object> data, Long expire)
    {
        HashMap<String, String> stringData = new HashMap<>();
        data.forEach((dataKey, value) ->
                stringData.put(dataKey, JsonUtils.toJson(value)));
        stringRedisTemplate.opsForHash().putAll(key, stringData);
        if (expire != null)
        {
            refreshExpire(key, expire);
        }
        log.info("[setHashMap] key: {}, data: {}, expire: {}", key, data, expire);
    }

    /**
     * 放入字符串类型的字符
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2023/12/0 20:16
     */
    public void setString(RedisKeyEnum Enum, String value, String... keyItems)
    {
        stringRedisTemplate.opsForValue().set(Enum.generateKey(keyItems), value, Enum.getExpire(), TimeUnit.SECONDS);
    }


    /**
     * 获取是否有对应的Key值存在
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2023/12/20 12:25
     */
    public boolean hasKey(RedisKeyEnum Enum, Object itemName)
    {
        String key = getFullKey(Enum, itemName);
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    /**
     * 获取是否有对应的Key值存在
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2023/12/20 12:25
     */
    public boolean hasKey(String key)
    {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }


    // endregion
    // region 排行榜实现
    // ===================== 排行榜实现 =====================

    /**
     * 有序集合添加之前没有的元素
     *
     * @param value 元素值 排行榜value-Key
     * @param score 分数
     * @return 是否添加成功
     * @author CAIXYPROMISE
     * @since 2023-12-29
     */
    public boolean zAdd(RedisKeyEnum rankKey, Object value, double score)
    {
        // 检查排行榜大小，并可能移除最低分数的记录
        manageRankSize(rankKey.getKey());
        // 添加新记录
        return Boolean.TRUE.equals(stringRedisTemplate.opsForZSet().add(rankKey.getKey(), value.toString(), score));
    }

    /**
     * 将HashMap转换为JSON字符串并添加到有序集合
     *
     * @param key   排行榜名称Key
     * @param map   要存储的HashMap
     * @param score 分数
     * @return 是否添加成功
     */
    public boolean zAddMap(RedisKeyEnum key, HashMap<String, Object> map, double score)
    {
        String valueAsJson = JsonUtils.mapToString(map);
        return zAdd(key, valueAsJson, score);
    }

    /**
     * 获取集合中元素的排名（从大到小排序）
     *
     * @param key   排行榜名称Key
     * @param value 元素值 排行榜value-Key
     * @return 获取到的排名
     * @author CAIXYPROMISE
     * @since 2023-12-29
     */
    public Long zGetRank(String key, Object value)
    {
        return stringRedisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 若集合中已有此元素，则此元素score+传入参数
     * 若没有此元素，则创建元素。
     *
     * @param key   排行榜名称Key
     * @param value 元素值 排行榜value-Key
     * @param score 分数
     * @author CAIXYPROMISE
     * @since 2023-12-29
     */
    public void zIncreamentScore(String key, Object value, double score)
    {
        stringRedisTemplate.opsForZSet().incrementScore(key, value.toString(), score);
    }

    /**
     * 检查排行榜大小，并可能移除最低分数的记录
     *
     * @param key 排行榜名称Key
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2023/1229 23:15
     */
    private void manageRankSize(String key)
    {
        Long size = zGetSize(key);
        if (size != null && size >= REDIS_INVOKE_RANK_MAX_SIZE)
        {
            // 移除最低分数的记录
            Set<ZSetOperations.TypedTuple<String>> lowestScoreSet =
                    stringRedisTemplate.opsForZSet().rangeWithScores(key, 0, 0);
            if (lowestScoreSet != null && !lowestScoreSet.isEmpty())
            {
                Double lowestScore = lowestScoreSet.iterator().next().getScore();
                if (lowestScore != null)
                {
                    stringRedisTemplate.opsForZSet().removeRangeByScore(key, lowestScore, lowestScore);
                }
            }
        }
    }

    /**
     * 对集合按照分数从小到大排序（默认）
     * 指定位置区间0，-1指排序所有元素
     * 得到的值带有score
     *
     * @return 排序结果
     * @author CAIXYPROMISE
     * @since 2023-12-29
     */
    public Set<ZSetOperations.TypedTuple<String>> zRangeWithScore(String key)
    {
        return stringRedisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
    }

    /**
     * 对集合按照分数从大到小排序
     *
     * @param key 排行榜名称Key
     * @author CAIXYPROMISE
     * @since 2023-12-29
     */
    public Set<ZSetOperations.TypedTuple<String>> zReverseRangeWithScore(String key)
    {
        return stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1);
    }

    /**
     * 获取有序集合的大小
     *
     * @param key 排行榜名称Key
     * @return 集合大小
     * @author CAIXYPROMISE
     * @since 2023-12-29
     */
    public Long zGetSize(String key)
    {
        return stringRedisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取key集合里面，value值的分数
     *
     * @param key   排行榜名称Key
     * @param value 元素值 排行榜value-Key
     * @return 获取到的分数
     * @author CAIXYPROMISE
     * @since 2023-12-29
     */
    public Double zGetScoreByValue(String key, Object value)
    {
        return stringRedisTemplate.opsForZSet().score(key, value.toString());
    }


    /**
     * 指定分数区间，从大到小排序
     *
     * @param key   排行榜名称Key
     * @param start 开始范围
     * @param end   结束方位
     * @return 排序结果榜集合
     * @author CAIXYPROMISE
     * @since 2023-12-29
     */
    public Set<ZSetOperations.TypedTuple<String>> zReverseRangeByScoreWithScores(String key, double start, double end)
    {
        return stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, start, end);
    }

    public void zRemove(String key, Object value)
    {
        stringRedisTemplate.opsForZSet().remove(key, value.toString());
    }


    // endregion

}
