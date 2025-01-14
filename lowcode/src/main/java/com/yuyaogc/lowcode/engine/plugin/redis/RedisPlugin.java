package com.yuyaogc.lowcode.engine.plugin.redis;

import com.yuyaogc.lowcode.engine.plugin.IPlugin;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.function.Consumer;


/**
 * RedisPlugin.
 * RedisPlugin 支持多个 Redis 服务端，只需要创建多个 RedisPlugin 对象
 * 对应这多个不同的 Redis 服务端即可。也支持多个 RedisPlugin 对象对应同一
 * Redis 服务的不同 database，具体例子见手册
 *
 * @author mjy
 */
public class RedisPlugin implements IPlugin {

    protected volatile boolean isStarted = false;

    protected String cacheName;

    protected String host;
    protected Integer port = null;
    protected Integer timeout = null;
    protected String password = null;
    protected Integer database = null;
    protected String clientName = null;

    protected ISerializer serializer = null;
    protected IKeyNamingPolicy keyNamingPolicy = null;
    protected JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

    public RedisPlugin(String cacheName, String host) {
        if (StringUtils.isBlank(cacheName))
            throw new IllegalArgumentException("cacheName can not be blank.");
        if (StringUtils.isBlank(host))
            throw new IllegalArgumentException("host can not be blank.");
        this.cacheName = cacheName.trim();
        this.host = host;
    }

    public RedisPlugin(String cacheName, String host, int port) {
        this(cacheName, host);
        this.port = port;
    }

    public RedisPlugin(String cacheName, String host, int port, int timeout) {
        this(cacheName, host, port);
        this.timeout = timeout;
    }

    public RedisPlugin(String cacheName, String host, int port, int timeout, String password) {
        this(cacheName, host, port, timeout);
        // 当 password 未指定时 jedis 底层不进行 auth 也可以进行操作
        // if (StrKit.isBlank(password))
        // throw new IllegalArgumentException("password can not be blank.");
        this.password = password;
    }

    public RedisPlugin(String cacheName, String host, int port, int timeout, String password, int database) {
        this(cacheName, host, port, timeout, password);
        this.database = database;
    }

    public RedisPlugin(String cacheName, String host, int port, int timeout, String password, int database, String clientName) {
        this(cacheName, host, port, timeout, password, database);
        if (StringUtils.isBlank(clientName))
            throw new IllegalArgumentException("clientName can not be blank.");
        this.clientName = clientName;
    }

    public RedisPlugin(String cacheName, String host, int port, String password) {
        this(cacheName, host, port, Protocol.DEFAULT_TIMEOUT, password);
    }

    public RedisPlugin(String cacheName, String host, String password) {
        this(cacheName, host, Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, password);
    }

    public boolean start() {
        if (isStarted) {
            return true;
        }

        JedisPool jedisPool;
        if (port != null && timeout != null && database != null && clientName != null)
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database, clientName);
        else if (port != null && timeout != null && database != null)
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
        else if (port != null && timeout != null)
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
            // else if (port != null && timeout != null)
            // jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
        else if (port != null)
            jedisPool = new JedisPool(jedisPoolConfig, host, port);
        else
            jedisPool = new JedisPool(jedisPoolConfig, host);

        if (serializer == null)
            serializer = FstSerializer.me;
        if (keyNamingPolicy == null)
            keyNamingPolicy = IKeyNamingPolicy.defaultKeyNamingPolicy;

        Cache cache = new Cache(cacheName, jedisPool, serializer, keyNamingPolicy);
        Redis.addCache(cache);

        isStarted = true;
        return true;
    }

    public boolean stop() {
        Cache cache = Redis.removeCache(cacheName);
        if (cache == Redis.mainCache)
            Redis.mainCache = null;
        cache.jedisPool.destroy();

        isStarted = false;
        return true;
    }

    /**
     * 当RedisPlugin 提供的设置属性仍然无法满足需求时，通过此方法获取到
     * JedisPoolConfig 对象，可对 redis 进行更加细致的配置
     * <pre>
     * 例如：
     * redisPlugin.getJedisPoolConfig().setMaxTotal(100);
     * </pre>
     */
    public JedisPoolConfig getJedisPoolConfig() {
        return jedisPoolConfig;
    }

    /**
     * lambda 方式配置 JedisPoolConfig
     * <pre>
     * 例子：
     *   RedisPlugin redisPlugin = new RedisPlugin(...);
     *   redisPlugin.config(c -> {
     *       c.setMaxIdle(123456);
     *   });
     * </pre>
     */
    public void config(Consumer<JedisPoolConfig> config) {
        config.accept(jedisPoolConfig);
    }

    // ---------

    public void setSerializer(ISerializer serializer) {
        this.serializer = serializer;
    }

    public void setKeyNamingPolicy(IKeyNamingPolicy keyNamingPolicy) {
        this.keyNamingPolicy = keyNamingPolicy;
    }

    // ---------

    public void setTestWhileIdle(boolean testWhileIdle) {
        jedisPoolConfig.setTestWhileIdle(testWhileIdle);
    }

    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
    }

    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
    }
}
