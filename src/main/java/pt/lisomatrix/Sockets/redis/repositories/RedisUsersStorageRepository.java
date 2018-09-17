package pt.lisomatrix.Sockets.redis.repositories;

import org.springframework.data.repository.CrudRepository;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;

public interface RedisUsersStorageRepository extends CrudRepository<RedisUserStorage, String> {
}
