package pt.lisomatrix.Sockets.redis.repositories;

import org.springframework.data.repository.CrudRepository;
import pt.lisomatrix.Sockets.models.User;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;

import java.util.List;
import java.util.Optional;

public interface RedisUsersStorageRepository extends CrudRepository<RedisUserStorage, String> {

    Optional<List<RedisUserStorage>> findFirstByUser(User user);

    Optional<List<RedisUserStorage>> findAllByDisconnected(Boolean disconnected);

    Optional<RedisUserStorage> findFirstByUserAndDisconnected(User user, Boolean Disconnected);

    Optional<RedisUserStorage> findFirstByUserIdAndDisconnected(Long userId, Boolean Disconnected);
}
