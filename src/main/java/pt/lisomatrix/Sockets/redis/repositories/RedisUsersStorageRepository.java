package pt.lisomatrix.Sockets.redis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.User;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Repository
public interface RedisUsersStorageRepository extends CrudRepository<RedisUserStorage, String> {

    @Async
    CompletableFuture<RedisUserStorage> findFirstById(String sessionId);

    Optional<List<RedisUserStorage>> findFirstByUser(User user);

    Optional<List<RedisUserStorage>> findAllByDisconnected(Boolean disconnected);

    Optional<RedisUserStorage> findFirstByUserAndDisconnected(User user, Boolean Disconnected);

    Optional<RedisUserStorage> findFirstByUserIdAndDisconnected(Long userId, Boolean Disconnected);
}
