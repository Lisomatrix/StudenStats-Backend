package pt.lisomatrix.Sockets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/***
 * Configures Redis Connection
 */
@Configuration
@EnableRedisRepositories(basePackages = "pt.lisomatrix.Sockets.redis.repositories")
public class RedisConfig {

    /*@Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }*/

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("192.168.1.10", 6379);

        redisStandaloneConfiguration.setPassword(RedisPassword.of("88998899"));


        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }
}
