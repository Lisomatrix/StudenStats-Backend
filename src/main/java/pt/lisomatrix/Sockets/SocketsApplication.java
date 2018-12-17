package pt.lisomatrix.Sockets;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import pt.lisomatrix.Sockets.models.Hour;
import pt.lisomatrix.Sockets.redis.models.RedisToken;
import pt.lisomatrix.Sockets.redis.repositories.RedisTokenRepository;
import pt.lisomatrix.Sockets.repositories.HoursRepository;
import pt.lisomatrix.Sockets.storage.FileStorageProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@ComponentScan("pt.lisomatrix.Sockets")
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class SocketsApplication {

    /***
     * REDIS Authentication Tokens Repository to get info and update
     */
    @Autowired
    private RedisTokenRepository redisTokenRepository;

    /***
     * Hours Repository to get info
     */
    @Autowired
    private HoursRepository hoursRepository;

    /***
     * Configure WebSocket Server
     *
     * @return
     */
	@Bean
	public SocketIOServer socketIOServer() {

	    // Update hours from resources file
	    generateHours();

		// Set configuration
		Configuration config = new Configuration();

		// Try to set the machine ip
        // If not possible set localhost
	    try {
	        config.setHostname("192.168.1.7");
            //config.setHostname("10.38.30.185");
        } catch (Exception e) {
	        config.setHostname("localhost");
        }

        // Set Port
		config.setPort(9092);


	    // Try to set SSL
        try {

            InputStream stream = SocketsApplication.class.getResourceAsStream("/pap_server.jks");
            config.setKeyStorePassword("88998899");

            config.setKeyStore(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set allowed origins
	    config.setOrigin("http://192.168.1.7:3000");
        // School
        //config.setOrigin("http://10.38.30.185:3000");
        //config.setOrigin("https://10.38.30.185:8080");
	    // PRODUCTION
        //config.setOrigin("https://192.168.1.7:8080");

        // Add authorization logic to WebSockets
        // If return true then User is authenticated
        // Otherwise Disconnect User
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

    /***
     * Helper to generate Hours for Database from hours.json file in resources
     *
     */
    public void generateHours() {

        try {

            List<Hour> currentHours = hoursRepository.findAll();

            if(currentHours.size() <= 0) {

                ObjectMapper mapper = new ObjectMapper();

                List<Hour> hours = mapper.readValue(new ClassPathResource("hours.json").getFile(),
                        new TypeReference<List<Hour>>(){});

                hoursRepository.saveAll(hours);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }



	public static void main(String[] args) {
		SpringApplication.run(SocketsApplication.class, args);
	}
}
