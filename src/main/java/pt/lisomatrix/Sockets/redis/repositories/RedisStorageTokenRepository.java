package pt.lisomatrix.Sockets.redis.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.redis.models.RedisStorageToken;

import java.util.Optional;

@Repository
public interface RedisStorageTokenRepository extends CrudRepository<RedisStorageToken, String> {
}
