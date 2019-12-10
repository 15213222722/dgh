package com.dgh.main.admin.redis;

import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName: JedisClusterConfig
 * @Description: reids集群配置/单机版配置
 * @Author dgh
 */
@Configuration
public class JedisConfig extends CachingConfigurerSupport {

	@Value("${spring.redis.host}")
	private String host;
	
	@Value("${spring.redis.port}")
	private String port;

	@Value("${spring.redis.jedis.pool.max-idle}")
	private Integer maxIdle;

	@Value("${spring.redis.jedis.pool.max-active}")
	private Integer maxActive;

	@Value("${spring.redis.jedis.pool.max-wait}")
	private Integer maxAWait;

	@Value("${spring.redis.timeout}")
	private Integer timeout;

	@Value("${spring.redis.password}")
	private String password;

	// 注解缓存默认过期时间，单位：分钟
	private final static int EXPIRE_TIME = 30;

	/**
	 * 缓存管理器
	 */
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(EXPIRE_TIME))
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.disableCachingNullValues();
		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
		return new RedisCacheManager(redisCacheWriter, config);
	}

	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
			throws UnknownHostException {
		RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
		template.setConnectionFactory(redisConnectionFactory);
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
		template.setDefaultSerializer(serializer);
		return template;
	}

	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			public Object generate(Object target, Method method, Object... objects) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName()).append(".").append(method.getName())
						.append(Arrays.toString(objects));
				return sb.toString();
			}
		};
	}

	/**
	 * jedis配置
	 *
	 * @return
	 */
	@Bean
	public JedisPoolConfig getJedisPoolConfig() {
		JedisPoolConfig jedisPool = new JedisPoolConfig();
		jedisPool.setMaxIdle(maxIdle); // 最大空闲数
		jedisPool.setMaxWaitMillis(maxAWait); // 最大等待时间
		jedisPool.setMaxTotal(maxActive); // 最大连接数
		return jedisPool;
	}

	/**
	 * 获取jedispool
	 *
	 * @return
	 */
	@Bean("jedisPool")
	public JedisPool getJedisPool() {
		JedisPoolConfig config = getJedisPoolConfig();
		JedisPool jedisPool = null;
		if (this.password.isEmpty()) {
			jedisPool = new JedisPool(config, host, Integer.valueOf(port), this.timeout);
		} else {
			jedisPool = new JedisPool(config, host, Integer.valueOf(port), this.timeout, this.password);
		}
		return jedisPool;
	}

//    public JedisCluster jedisCluster;
//
//    @Value("${spring.redis.cluster.nodes}")
//    private String nodes;
//
//    @Value("${spring.redis.timeout}")
//    private int timeout;
//
//    @Value("${spring.redis.soTimeout}")
//    private int soTimeout;
//
//    @Value("${spring.redis.jedis.pool.max-idle}")
//    private int maxIdle;
//
//    @Value("${spring.redis.jedis.pool.max-wait}")
//    private int maxWait;
//
//    @Value("${spring.redis.jedis.pool.testOnBorrow}")
//    private boolean testOnBorrow;
//
//    @Value("${spring.redis.jedis.pool.testOnReturn}")
//    private boolean testOnReturn;
//
//    @Value("${spring.redis.jedis.pool.max-active}")
//    private int maxActive;
//
//    @Value("${spring.redis.maxAttempts}")
//    private int maxAttempts;
//
//    @Value("${spring.redis.password}")
//    private String password;
//
//    @Bean("jedisCluster")
//    public JedisCluster getJedisCluster() {
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxTotal(maxActive);
//        config.setMaxIdle(maxIdle);
//        config.setMaxWaitMillis(maxWait);
//        config.setTestOnBorrow(testOnBorrow);
//        config.setTestOnReturn(testOnReturn);
//        String[] urls = nodes.split(",");
//        Set<HostAndPort> serverSet = new HashSet<HostAndPort>();
//        for (String url : urls) {
//            String[] urlSplit = url.split(":");
//            serverSet.add(new HostAndPort(urlSplit[0], Integer.valueOf(urlSplit[1])));
//        }
//        jedisCluster = new JedisCluster(serverSet, timeout, soTimeout,maxAttempts,password, config);
//
//        return jedisCluster;
//    }

}
