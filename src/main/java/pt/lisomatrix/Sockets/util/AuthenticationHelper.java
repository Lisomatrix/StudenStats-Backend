package pt.lisomatrix.Sockets.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.redis.models.RedisToken;
import pt.lisomatrix.Sockets.redis.repositories.RedisTokenRepository;

import java.util.Optional;

@Component
public class AuthenticationHelper {

    @Autowired
    private RedisTokenRepository redisTokenRepository;

    public Authenticated authenticate(String token) {

        Optional<RedisToken> foundToken = redisTokenRepository.findById(token);

        if(foundToken.isPresent()) {

            RedisToken redisToken = foundToken.get();

            if(!redisToken.isUsed()) {
                redisToken.setUsed(true);

                redisTokenRepository.save(redisToken);

                return new Authenticated(true, redisToken.getRole(), redisToken.getUserId(), redisToken.getIpAddress());
            }
        }

        return new Authenticated(false, "", 0l, "");
    }

}
