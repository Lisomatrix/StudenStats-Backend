package pt.lisomatrix.Sockets.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/*@Component
public class EmbededRedis {

    @Value("${spring.redis.port}")
    private int port;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = RedisServer.builder()
                .port(port)
                //.redisExecProvider(customRedisExec) //com.github.kstyrc (not com.orange.redis-embedded)
                //.setting("maxmemory 128M") //maxheap 128M
                .build();
        redisServer.start();
        System.out.println("##################################");
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}*/
