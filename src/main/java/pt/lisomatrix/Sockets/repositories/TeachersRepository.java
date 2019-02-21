package pt.lisomatrix.Sockets.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.models.User;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public interface TeachersRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findFirstByUser(User user);

    @Query(value = "select * from teacher where user_id = (select user_id from user_account where email = :username) limit 1;", nativeQuery = true)
    Optional<Teacher> findFirstByUsername(@Param("username") String username);

    @Query(value = "select * from teacher where user_id = :userId limit 1;", nativeQuery = true)
    Optional<Teacher> findFirstByUserId(@Param("userId") long userId);

    @Query(value = "select * from teacher where user_id = :userId limit 1;", nativeQuery = true)
    CompletableFuture<Teacher> findFirstByUserIdAsync(@Param("userId") long userId);
}
