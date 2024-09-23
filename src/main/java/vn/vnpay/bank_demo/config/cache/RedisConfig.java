package vn.vnpay.bank_demo.config.cache;

import io.lettuce.core.ReadFrom;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("6379")
    private String redisPort;
    @Value("localhost")
    private String redisHost;

    @Bean
    public RedisSerializer<String> stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public RedisSerializer<Object> objectRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(stringRedisSerializer());
        template.setHashKeySerializer(stringRedisSerializer());
        template.setHashValueSerializer(stringRedisSerializer());
        template.setValueSerializer(stringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @Primary
    public RedisConnectionFactory lettuceConnectionFactory(GenericObjectPoolConfig<?> genericObjectPoolConfig) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(Integer.parseInt(redisPort));
//        config.setPassword(null);
        config.setDatabase(0);

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .readFrom(ReadFrom.MASTER_PREFERRED)
                .clientName("myClient")
                .build();
        return new LettuceConnectionFactory(config, clientConfig);
    }

    @Bean
    public GenericObjectPoolConfig<?> genericObjectPoolConfig() {
        GenericObjectPoolConfig<?> pool = new GenericObjectPoolConfig<>();
        pool.setMaxTotal(128);
        pool.setMaxIdle(18);
        pool.setMinIdle(8);
        return pool;
    }
}
