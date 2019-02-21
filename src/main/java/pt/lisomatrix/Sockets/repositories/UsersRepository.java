package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByUsername(String email);

    Optional<User> findByRegistrationCode(String registrationCode);

    @Query(value = "select * from user_account", nativeQuery = true)
    @Async
    CompletableFuture<List<User>> findAllAsync();
}
