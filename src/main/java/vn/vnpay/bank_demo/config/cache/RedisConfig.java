package vn.vnpay.bank_demo.config.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//@Configuration
public class RedisConfig {
//    @Value("6379")
//    private String redisPort;
//    @Value("localhost")
//    private String redisHost;
//
//    //Kết nối tới redis của máy
//    @Bean
//    JedisConnectionFactory jedisConnectionFactory(){
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//        redisStandaloneConfiguration.setHostName(redisHost);
//        redisStandaloneConfiguration.setPort(Integer.parseInt(redisPort));
//        return new JedisConnectionFactory(redisStandaloneConfiguration);
//    }
//
//    //Thao tác trong code xuống redis
//    @Bean
//    RedisTemplate<String, Object> redisTemplate(){
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(jedisConnectionFactory());
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
//        return redisTemplate;
//    }
}
