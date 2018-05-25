package cn.partytime.redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by task on 16/6/27.
 */

@Service("redisService")
public class RedisService {
    private Logger logger = LoggerFactory.getLogger(RedisService.class);


    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 设置过期时间
     *
     * @param key     key
     * @param seconds 多少秒以后过期
     */
    public void expire(String key, long seconds) {
        if (key == null || key.equals("")) {
            return;
        }
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);

    }

    /**
     * 判断是否含有key
     *
     * @param key
     * @return
     */
    public boolean isEXIST(String key) {
        if (key == null || key.equals("")) {
            return false;
        }
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取所有key
     *
     * @param prefix
     * @return
     */
    public Set<String> getKeys(String prefix) {
        if (prefix == null || prefix.equals("")) {
            return null;
        }
        return redisTemplate.keys(prefix + "*");
    }


    /**
     * 设置一个key value结构
     *
     * @param key
     * @param object
     * @param seconds 过期时间
     */
    public void set(String key, Object object, int seconds) {
        if (key == null || key.equals("")) {
            return;
        }
        redisTemplate.opsForValue().set(key, object, seconds, TimeUnit.SECONDS);
    }

    public void set(String key, Object object) {
        if (key == null || key.equals("")) {
            return;
        }
        redisTemplate.opsForValue().set(key, object);
    }


    /**
     * 通过key获取value的值
     *
     * @param key
     */
    public Object get(String key) {
        String value = null;
        Object object = redisTemplate.opsForValue().get(key);
        if (object != null) {
            return object;
        }
        return null;
    }

    /**
     * 递增
     *
     * @param key
     */
    public void incrKey(String key, int value) {
        redisTemplate.opsForValue().increment(key, value);
    }

    /**
     * 递减
     *
     * @param key
     */
    public void decrKey(String key, int value) {
        redisTemplate.opsForValue().increment(key, value);
    }

    /**************************************************************list***************************************************************/
    /**
     * 向list集合中添加数据
     *
     * @param key
     * @param list
     * @return
     */
    public Long setListToList(String key, List list) {
        if (key == null || key.equals("")) {
            return 0L;
        }
        return redisTemplate.opsForList().leftPushAll(key, list);
    }


    public long getListLength(String key){
        if (key == null || key.equals("")) {
            return 0L;
        }
        return redisTemplate.opsForList().size(key);
    }

    public long setValueToList(String key, Object object) {
        if (key == null || key.equals("")) {
            return 0L;
        }
        return redisTemplate.opsForList().leftPush(key, object);
    }

    public long setValueToListFromRight(String key, Object object) {
        if (key == null || key.equals("")) {
            return 0L;
        }
        return redisTemplate.opsForList().rightPush(key, object);
    }


    public Long setListToList(String key, Object[] objects) {
        if (key == null || key.equals("")) {
            return 0L;
        }
        return redisTemplate.opsForList().leftPushAll(key, objects);
    }

    public List<String> getALLFromList(String key) {
        if (key == null || key.equals("")) {
            return null;
        }
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 从缓存中提取一个元素
     *
     * @param key
     * @return
     */
    public Object popFromList(String key) {
        if (key == null || key.equals("")) {
            return null;
        }
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从缓存中提取一个元素
     *
     * @param key
     * @return
     */
    public Object popFromListFromRight(String key) {
        if (key == null || key.equals("")) {
            return null;
        }
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取redis中list数量
     *
     * @param key
     * @return
     */
    public long listsize(String key) {
        if (key == null || key.equals("")) {
            return 0;
        }
        return redisTemplate.opsForList().size(key);
    }
    /**************************************************************list***************************************************************/

/**************************************************************sortSet***************************************************************/
    /**
     * 向sortSet集合中添加数据
     *
     * @param key
     * @param score
     * @param value
     * @return
     */
    public boolean setSortSet(String key, double score, Object value) {
        if (key == null || key.equals("")) {
            return false;
        }
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 读取sortSet数量
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public long getSortSetSize(String key, long min, long max) {
        if (key == null || key.equals("")) {
            return 0;
        }
        return redisTemplate.opsForZSet().count(key, min, max);
    }


    /**
     * 删除sortset中的数据
     *
     * @param key
     * @param value
     */
    public long deleteSortData(String key, String value) {
        if (key == null || key.equals("")) {
            return 0;
        }
        return redisTemplate.opsForZSet().remove(key, value);
    }

    /**
     * 通过score获取sorset范围内的值
     *
     * @param key
     * @param minScore
     * @param maxScore
     * @return
     */
    public Set<String> findSortSetWithInScore(String key, Double minScore, Double maxScore) {
        if (key == null || key.equals("")) {
            return null;
        }
        return redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore);
    }

    public long findSortSetSize(String key){
        return redisTemplate.opsForZSet().size(key);
    }


    /**
     * 通过范围获取sortSet集合数据
     *
     * @param key
     * @param startRange
     * @param endRange
     * @param orderByDesc
     * @return
     */
    public Set<String> getSortSetByRnage(String key, long startRange, long endRange, boolean orderByDesc) {
        if (orderByDesc) {
            return redisTemplate.opsForZSet().range(key, startRange, endRange);
        } else {
            return redisTemplate.opsForZSet().reverseRange(key, startRange, endRange);
        }
    }


    /**
     * 通过score获取sorset范围内的值
     *
     * @param key
     * @param minScore
     * @param maxScore
     * @return
     */
    public Set<String> findSortSetRangeByScoreWithScores(String key, Double minScore, Double maxScore,long startRange, long endRange,boolean orderByDesc) {
        if (key == null || key.equals("")) {
            return null;
        }
        if(orderByDesc){
            return redisTemplate.opsForZSet().rangeByScore(key,minScore,maxScore,startRange,endRange);
        }else{
            return redisTemplate.opsForZSet().reverseRangeByScore(key,minScore,maxScore,startRange,endRange);
        }
    }


    /**************************************************************sortSet***************************************************************/
    /**
     * redis订阅发布
     * @param key
     * @param value
     */
    public void subPub(String key,String value){
        redisTemplate.convertAndSend(key,value);
    }


    public void deleteByKey(String key){
        this.expire(key,0);
    }
}
