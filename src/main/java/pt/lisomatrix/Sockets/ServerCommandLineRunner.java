package pt.lisomatrix.Sockets;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.redis.models.RedisStorageToken;
import pt.lisomatrix.Sockets.redis.models.RedisToken;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;
import pt.lisomatrix.Sockets.redis.repositories.RedisStorageTokenRepository;
import pt.lisomatrix.Sockets.redis.repositories.RedisTokenRepository;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.storage.StorageService;

import java.util.Optional;

@Component
public class ServerCommandLineRunner implements CommandLineRunner {

    private final SocketIOServer server;

    private RedisUsersStorageRepository redisUsersStorageRepository;

    private RedisTokenRepository redisTokenRepository;

    @Autowired
    public ServerCommandLineRunner(SocketIOServer server, RedisUsersStorageRepository redisUsersStorageRepository) {
        this.server = server;
        this.redisUsersStorageRepository = redisUsersStorageRepository;
        this.redisTokenRepository = redisTokenRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        /*server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {

                // Send session id to user
                socketIOClient.sendEvent("session", socketIOClient.getSessionId().toString().replace("-", ""));

                // Get token from the handshake data
                String usedToken = socketIOClient.getHandshakeData().getSingleUrlParam("token");

                // Get token from redis
                Optional<RedisToken> foundRedisToken = redisTokenRepository.findById(usedToken);

                // If found
                if(foundRedisToken.isPresent()) {
                    // Get token object
                    RedisToken redisToken = foundRedisToken.get();

                    // Create RedisUserStorage instance and populate it
                    RedisUserStorage redisUserStorage = new RedisUserStorage();

                    redisUserStorage.setRole(redisToken.getRole());

                    // Save to redis
                    redisUsersStorageRepository.save(redisUserStorage);

                    // Delete token
                    redisTokenRepository.deleteById(redisToken.getToken());

                } else {
                    // If token found disconnect the user
                    socketIOClient.disconnect();
                }
            }
        });*/


        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                // TODO REMOVE REDIS MEMORY STORAGE
            }
        });

        server.start();
    }
}
