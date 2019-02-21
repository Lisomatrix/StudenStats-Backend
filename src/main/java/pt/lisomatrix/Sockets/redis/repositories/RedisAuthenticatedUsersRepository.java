package pt.lisomatrix.Sockets.redis.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.redis.models.AuthenticatedUser;

import java.util.Optional;

@Repository
public interface RedisAuthenticatedUsersRepository extends CrudRepository<AuthenticatedUser, String> {

    //Optional<AuthenticatedUser> findFirstByToken(String token);
}
