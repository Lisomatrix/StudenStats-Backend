package pt.lisomatrix.Sockets;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import pt.lisomatrix.Sockets.redis.models.RedisToken;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;
import pt.lisomatrix.Sockets.redis.repositories.RedisTokenRepository;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.repositories.TokensRepository;
import pt.lisomatrix.Sockets.repositories.UsersRepository;
import pt.lisomatrix.Sockets.storage.FileStorageProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan("pt.lisomatrix.Sockets")
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class SocketsApplication {

	@Autowired
	private TokensRepository tokensRepository;

    @Autowired
    private RedisTokenRepository redisTokenRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RedisUsersStorageRepository redisUsersStorageRepository;

	@Bean
	public SocketIOServer socketIOServer() {

		// Set configuration
		Configuration config = new Configuration();

		// Try to set the machine ip
        // If no possible set localhost
	    try {
	        config.setHostname(InetAddress.getLocalHost().getHostAddress());
        } catch (Exception e) {
	        config.setHostname("localhost");
        }

        // Set Port
		config.setPort(9092);


	    // Try to set SSL
        try {

            InputStream stream = SocketsApplication.class.getResourceAsStream("/keystore.jks");
            config.setKeyStorePassword("test1234");
            config.setKeyStore(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set allowed origins
	    config.setOrigin("http://192.168.1.3:3000");
        //config.setOrigin("https://192.168.1.3:8080");


		config.setAuthorizationListener(new AuthorizationListener() {
			@Override
			public boolean isAuthorized(HandshakeData handshakeData) {

                // Get token from the query of the request
                String token = handshakeData.getSingleUrlParam("token");

                // If token is sent
                if(token != null) {
                    // Get token from redis
                    Optional<RedisToken> foundToken = redisTokenRepository.findById(token);

                    // If found
                    if(foundToken.isPresent()) {
                        // Get token
                        RedisToken redisToken = foundToken.get();
                        // If it is not used
                        if(!redisToken.isUsed()) {
                            // Check if the authentication IP is the same as used in http auth
                            if(redisToken.getIpAddress().equals(handshakeData.getLocal().getHostString())) {

                                // Set to used
                                redisToken.setUsed(true);

                                // Create user storage
                                RedisUserStorage redisUserStorage = new RedisUserStorage();

                                // Populate user storage
                                redisUserStorage.setToken(redisToken.getToken());
                                redisUserStorage.setRole(redisToken.getRole());

                                // Save user storage to redis
                                redisUsersStorageRepository.save(redisUserStorage);

                                // Update token
                                redisTokenRepository.save(redisToken);


                                return true;

                            } else {
                                return false;
                            }

                        } else {
                            return false;
                        }
                    } else {

                        return false;
                    }
                } else {
                    return false;
                }
			}
		});

		return new SocketIOServer(config);
	}


	public static void main(String[] args) {
		SpringApplication.run(SocketsApplication.class, args);
	}
}
