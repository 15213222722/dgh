package com.dgh.main.admin.redis.service.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dgh.main.admin.redis.service.JedisService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

/**
 * 
 * @author dgh
 *
 */
@Service("jedisService")
public class JedisServiceImpl implements JedisService {

//	@Autowired
//	private JedisCluster jedisCluster;
	@Autowired
	JedisPool jedisPool;

	private static final String LOCK_SUCCESS = "OK";
//	private static final String SET_IF_NOT_EXIST = "NX";// 只能取NX或者XX，如果取NX，则只有当key不存在是才进行set，如果取XX，则只有当key已经存在时才进行set
//	private static final String SET_WITH_EXPIRE_TIME = "PX";// 只能取EX或者PX，代表数据过期时间的单位，EX代表秒，PX代表毫秒。
	private static final Long RELEASE_SUCCESS = 1L;

	public boolean exists(String key) {
		Jedis jedis = null;
		boolean flag = false;
		try {
			jedis = jedisPool.getResource();
			flag = jedis.exists(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return flag;
	}

	public String set(String key, String value, int seconds) {
		Jedis jedis = null;
		String str = "";
		try {
			jedis = jedisPool.getResource();
			str = jedis.set(key, value);
			if (seconds != 0) {
				jedis.expire(key, seconds);
			}
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return str;
	}

	public String setObject(String key, Object obj, int expireTime) throws JsonProcessingException {
		Jedis jedis = null;
		String str = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			jedis = jedisPool.getResource();
			str = jedis.setex(key, expireTime, mapper.writeValueAsString(obj));
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return str;
	}

	public String getSet(String key, String value, int seconds) {
		Jedis jedis = null;
		String str = "";
		try {
			jedis = jedisPool.getResource();
			str = jedis.getSet(key, value);
			jedis.expire(key, seconds);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return str;
	}

	public String get(String key) {
		Jedis jedis = null;
		String str = "";
		try {
			jedis = jedisPool.getResource();
			str = jedis.get(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return str;
	}

	public void delKey(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public Map<String, Object> getMapData(String key) throws JsonParseException, JsonMappingException, IOException {
		Jedis jedis = null;
		String str = "";
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		try {
			jedis = jedisPool.getResource();
			str = jedis.get(key);
			map = mapper.readValue(str, new TypeReference<Map<String, Object>>() {
			});
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return map;
	}

	/**
	 * 如为第一次，则加上锁，每次调用值会自动加1
	 */
	public boolean lock(String key, int seconds) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if (jedis.incr(key) == 1) {
				jedis.expire(key, seconds);
				return true;
			}
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return false;
	}

	/**
	 * 尝试获取分布式锁,加锁
	 * 
	 * @param lockKey    锁
	 * @param requestId  请求标识
	 * @param expireTime 超期时间
	 * @return 是否获取成功
	 */
	public boolean lock(String lockKey, String requestId, int expireTime) {
		Jedis jedis = null;
		String result = "";
		try{
			jedis = jedisPool.getResource();
			SetParams setParams = new SetParams();
			setParams.nx();//只能取NX或者XX，如果取NX，则只有当key不存在是才进行set，如果取XX，则只有当key已经存在时才进行set
			setParams.px(expireTime);//只能取EX或者PX，代表数据过期时间的单位，EX代表秒，PX代表毫秒。
			result = jedis.set(lockKey, requestId, setParams);
			if (LOCK_SUCCESS.equals(result)) {
				return true;
			}
		}finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return false;
	}

	public void unlock(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * 释放分布式锁
	 * 
	 * @param lockKey   锁
	 * @param requestId 请求标识
	 * @return 是否释放成功
	 */
	public boolean releaseDistributedLock(String lockKey, String requestId) {
		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Object result = jedis.eval(script, Collections.singletonList(lockKey),
					Collections.singletonList(requestId));
			if (RELEASE_SUCCESS.equals(result)) {
				return true;
			}
		}finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return false;
	}

	public String getLocakValue(String key) {
		String str = "";
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			str = jedis.get(key);
		}finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return str;
	}

	@Override
	public Long getRetryCount(String lockKey) {
		Long count = 0l;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			count = jedis.incr(lockKey);
		}finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return count;
	}

	@Override
	public Long expireKey(String key, int expireTime) {
		Jedis jedis = null;
		Long count = 0l;
		try {
			jedis = jedisPool.getResource();
			count = jedis.expire(key, expireTime);
		}finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return count;
	}
	
	

}
