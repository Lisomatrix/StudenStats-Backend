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

        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {

                // Get connection token
                String usedToken = socketIOClient.getHandshakeData().getSingleUrlParam("token");

                // If token string was found
                if(!usedToken.equals("")) {

                    // Get redis token
                    Optional<RedisToken> foundRedisToken = redisTokenRepository.findById(usedToken);

                    // If redis token found
                    if(foundRedisToken.isPresent()) {

                        // Get redis token found
                        RedisToken redisToken = foundRedisToken.get();

                        // Get Redis User Storage
                        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(redisToken.getToken());

                        // If redis user storage was found
                        if(foundRedisUserStorage.isPresent()) {
                            // Get redis user storage
                            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

                            // delete redis user storage and redis token
                            redisUsersStorageRepository.delete(redisUserStorage);
                            redisTokenRepository.delete(redisToken);

                        } else {
                            // delete redis token
                            redisTokenRepository.delete(redisToken);
                        }

                    }

                }


            }
        });

        server.start();
    }
}
