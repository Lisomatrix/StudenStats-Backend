package pt.lisomatrix.Sockets.modules;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.redis.models.RedisStorageToken;
import pt.lisomatrix.Sockets.redis.repositories.RedisStorageTokenRepository;

import java.util.UUID;

@Component
public class StorageAuthorizationModule {

    @Autowired
    private SocketIOServer server;

    @Autowired
    private RedisStorageTokenRepository redisStorageTokenRepository;

    @OnEvent(value = "StorageAuthorization")
    public void onEvent(SocketIOClient client, AckRequest request, String sessionId) {

        String uniqueId = UUID.randomUUID().toString().replace("-", "");

        RedisStorageToken redisStorageToken = new RedisStorageToken();

        redisStorageToken.setToken(uniqueId);
        redisStorageToken.setSessionId(sessionId);

        redisStorageTokenRepository.save(redisStorageToken);
    }
}
