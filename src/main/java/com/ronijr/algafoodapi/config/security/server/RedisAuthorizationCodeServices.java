package com.ronijr.algafoodapi.config.security.server;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public class RedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    private static final boolean springDataRedis_2_0 = ClassUtils.isPresent(
            "org.springframework.data.redis.connection.RedisStandaloneConfiguration",
            RedisAuthorizationCodeServices.class.getClassLoader());

    private static final String AUTH_CODE = "auth_code:";

    private final RedisConnectionFactory connectionFactory;

    private String prefix = "";

    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    private Method redisConnectionSet_2_0;

    /**
     * Default constructor.
     *
     * @param connectionFactory the connection factory which should be used to obtain a connection to Redis
     */
    public RedisAuthorizationCodeServices(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        if (springDataRedis_2_0) {
            this.loadRedisConnectionMethods_2_0();
        }
    }

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        byte[] key = serializeKey(AUTH_CODE + code);
        byte[] auth = serialize(authentication);

        RedisConnection conn = getConnection();
        try {
            if (springDataRedis_2_0) {
                try {
                    this.redisConnectionSet_2_0.invoke(conn, key, auth);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                conn.set(key, auth);
            }
        }
        finally {
            conn.close();
        }
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        byte[] key = serializeKey(AUTH_CODE + code);

        List<Object> results = null;
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.get(key);
            conn.del(key);
            results = conn.closePipeline();
        }
        finally {
            conn.close();
        }

        if (results == null) {
            return null;
        }
        byte[] bytes = (byte[]) results.get(0);
        return deserializeAuthentication(bytes);
    }

    private void loadRedisConnectionMethods_2_0() {
        this.redisConnectionSet_2_0 = ReflectionUtils.findMethod(
                RedisConnection.class, "set", byte[].class, byte[].class);
    }

    private byte[] serializeKey(String object) {
        return serialize(prefix + object);
    }

    private byte[] serialize(Object object) {
        return serializationStrategy.serialize(object);
    }

    private byte[] serialize(String string) {
        return serializationStrategy.serialize(string);
    }

    private RedisConnection getConnection() {
        return connectionFactory.getConnection();
    }

    private OAuth2Authentication deserializeAuthentication(byte[] bytes) {
        return serializationStrategy.deserialize(bytes, OAuth2Authentication.class);
    }

    public void setSerializationStrategy(RedisTokenStoreSerializationStrategy serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}