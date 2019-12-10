package com.dgh.main.admin.redis.service;

import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * 
 * @author dgh
 *
 */
public interface JedisService {

    /**
     * 是否存在
     * @param key
     * @return
     */
    public boolean exists(String key);

    /**
     * 缓存set值
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public String set(String key,String value,int seconds);
    
    /**
     * 缓存对象
     * @param key
     * @param obj
     * @param expireTime
     * @return
     */
    public String setObject(String key, Object obj, int expireTime) throws JsonProcessingException;

    /**
     * 重新缓存getSet值
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public String getSet(String key,String value, int seconds);

    /**
     * 获取set值
     * @param key
     * @return
     */
    public String get(String key);

    /**
     * 删除key
     * @param key
     */
    public void delKey(String key);
    
    /**
     * 获取map格式的数据  
     * @param key
     * @return
     */
    public Map<String ,Object> getMapData(String key) throws JsonParseException, JsonMappingException, IOException;
    
    /**
     * 加锁，避免重复提交 
     * @param key
     * @param seconds
     * @return
     */
    public boolean lock(String key,int seconds);
    
    /**
     * 解锁  
     * @param key
     */
    public void unlock(String key);    
    
    /**
     * 统计锁定次数
     * @param key
     * @return
     */
    public String getLocakValue(String key);

    /**
     * 分布式加锁代码
     * @param lockKey
     * @param requestId
     * @param expireTime
     * @return
     */
    public boolean lock(String lockKey, String requestId, int expireTime);
	
	/**
	 * 分布式解锁代码
	 * @param lockKey
	 * @param requestId
	 * @return
	 */
    public boolean releaseDistributedLock(String lockKey, String requestId);
    
    /**
     * 
     * @param lockKey
     * @return
     */
    public Long getRetryCount(String lockKey);
    
    /**
     * 添加超时时间
     * @param key
     * @param expireTime
     * @return
     */
    public Long expireKey(String key, int expireTime);
    
}
