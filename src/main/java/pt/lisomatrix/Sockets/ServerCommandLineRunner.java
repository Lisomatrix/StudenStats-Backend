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

import java.util.Date;
import java.util.Optional;

/***
 * This class takes care of configure the WebSocket Server Events
 */
@Component
public class ServerCommandLineRunner implements CommandLineRunner {

    /***
     * WebSockets server object
     */
    private final SocketIOServer server;

    /***
     * REDIS Users Storage repository to get in ram info
     */
    private RedisUsersStorageRepository redisUsersStorageRepository;

    /***
     * REDIS Authentication Tokens Repository to get info and update
     */
    private RedisTokenRepository redisTokenRepository;

    /***
     * Initialize all class dependencies
     *
     * @param server
     * @param redisUsersStorageRepository
     * @param redisTokenRepository
     */
    @Autowired
    public ServerCommandLineRunner(SocketIOServer server, RedisUsersStorageRepository redisUsersStorageRepository, RedisTokenRepository redisTokenRepository) {
        this.server = server;
        this.redisUsersStorageRepository = redisUsersStorageRepository;
        this.redisTokenRepository = redisTokenRepository;
    }

    /***
     * Configure WebSocket Server Events and start it
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {

        // Listen to disconnect Event
        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {

                // Get user Redis User Storage
                Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(socketIOClient.getSessionId().toString());

                // Check if found
                if(foundRedisUserStorage.isPresent()) {

                    // Get Redis User Storage
                    RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

                    // Update last connected date and connected status;
                    Date actualDate = new Date();

                    redisUserStorage.setDate(actualDate.toString());
                    redisUserStorage.setDisconnected(true);

                    redisUsersStorageRepository.save(redisUserStorage);

                    // Delete it from redis
                    //redisUsersStorageRepository.delete(redisUserStorage);

                    // Get used token to connect
                    String usedToken = socketIOClient.getHandshakeData().getSingleUrlParam("token");

                    // Check if not null
                    if(!usedToken.trim().equals("")) {

                        // Get redis token
                        Optional<RedisToken> foundRedisToken = redisTokenRepository.findById(usedToken);

                        // Check if found
                        if(foundRedisToken.isPresent()) {

                            // Get redis token
                            RedisToken redisToken = foundRedisToken.get();

                            // Delete redis token from database
                            redisTokenRepository.delete(redisToken);

                        } else {
                            socketIOClient.disconnect();
                        }
                    }

                } else {
                    socketIOClient.disconnect();
                }
            }
        });

        // Start WebSocket Server
        server.start();
    }
}
