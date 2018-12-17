package pt.lisomatrix.Sockets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import pt.lisomatrix.Sockets.redis.MessagePublisher;

/***
 * Configures Redis Connection
 */
@Configuration
@EnableRedisRepositories(basePackages = "pt.lisomatrix.Sockets.redis.repositories")
public class RedisConfig {

    /***
     * returns an instance of JedisConnectionfactory
     *
     * @return JedisConnectionFactory
     */
    /*@Bean
    JedisConnectionFactory jedisConnectionFactory() {

        // Establish connection with redis
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("localhost", 6379);

        // Returns redis connection
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }*/

    /***
     * returns an instance of RedisTemplate
     *
     * @return RedisTemplate<String, Object>
     */
    /*@Bean
    public RedisTemplate<String, Object> redisTemplate() {
        // Create template
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        // Set template connection and serializer
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));

        // returns template
        return template;
    }*/


    /***
     * Returns MessageListenerAdapter instance
     *
     * @return MessageListenerAdapter
     */
    /*@Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new RedisMessageSubscriber());
    }*/

    /***
     * Returns RedisMessageListenerContainer instance
     *
     * @return RedisMessageListenerContainer
     */
    /*@Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(), topic());
        return container;
    }*/

    /***
     * Returns MessagePublisher instance
     *
     * @return MessagePublisher
     */
    /*@Bean
    MessagePublisher redisPublisher() {
        return new RedisMessagePublisher(redisTemplate(), topic());
    }*/

    /***
     * Returns ChannelTopic instance
     * @return ChannelTopic
     */
    /*@Bean
    ChannelTopic topic() {
        return new ChannelTopic("pubsub:queue");
    }*/
}
