package pt.lisomatrix.Sockets.redis.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.redis.models.RedisStorageServer;

@Repository
public interface RedisStorageServersRepository extends CrudRepository<RedisStorageServer, Long> {
}
