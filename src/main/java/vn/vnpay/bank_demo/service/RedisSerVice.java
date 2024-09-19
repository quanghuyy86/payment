package vn.vnpay.bank_demo.service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

public class RedisSerVice {
    private JedisPool jedisPool;

    //Khởi tạo Redis Pool
    public RedisSerVice(String redisHost, String redisPort){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(128);
        jedisPoolConfig.setMaxIdle(16);
        jedisPoolConfig.setMinIdle(8);
        this.jedisPool = new JedisPool(jedisPoolConfig, redisHost, Integer.parseInt(redisPort));
    }

    // Hàm để set dữ liệu vào Redis
    public void setDataToRedis(String bankCode, String tokenKey, Map<String, String> jsonData) {
        try (Jedis jedis = jedisPool.getResource()) {
            // Sử dụng hset để lưu trữ dữ liệu theo công thức yêu cầu
            jedis.hset(bankCode, tokenKey, jsonData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
