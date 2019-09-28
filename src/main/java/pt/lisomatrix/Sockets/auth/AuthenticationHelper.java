package pt.lisomatrix.Sockets.auth;

import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.models.User;
import pt.lisomatrix.Sockets.redis.models.AuthenticatedUser;
import pt.lisomatrix.Sockets.redis.repositories.RedisAuthenticatedUsersRepository;

@Component
public class AuthenticationHelper {

    private RedisAuthenticatedUsersRepository redisAuthenticatedUsersRepository;

    public AuthenticationHelper(RedisAuthenticatedUsersRepository redisAuthenticatedUsersRepository) {
        this.redisAuthenticatedUsersRepository = redisAuthenticatedUsersRepository;
    }

    public void setAuthenticatedUser(User user, String token) {

        AuthenticatedUser authenticatedUser = new AuthenticatedUser();

        authenticatedUser.setRole(user.getRole().getRole());
        authenticatedUser.setToken(token);
        authenticatedUser.setUserId(user.getUserId());

        redisAuthenticatedUsersRepository.save(authenticatedUser);
    }

}
