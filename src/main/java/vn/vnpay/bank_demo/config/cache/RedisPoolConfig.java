package vn.vnpay.bank_demo.config.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisPoolConfig {
    @Value("6379")
    private String redisPort;
    @Value("localhost")
    private String redisHost;
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);  // Số lượng kết nối tối đa
        poolConfig.setMaxIdle(16);    // Số lượng kết nối tối đa rảnh rỗi
        poolConfig.setMinIdle(8);     // Số lượng kết nối tối thiểu rảnh rỗi
        poolConfig.setJmxEnabled(false);  // Tắt tính năng JM
        return poolConfig;
    }
    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(jedisPoolConfig(), redisHost, Integer.parseInt(redisPort));
    }


}
