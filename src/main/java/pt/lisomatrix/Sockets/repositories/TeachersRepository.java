package pt.lisomatrix.Sockets.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.models.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public interface TeachersRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findFirstByUser(User user);

    @Query(value = "select * from teacher where user_id = (select user_id from user_account where email = :username) limit 1;", nativeQuery = true)
    Optional<Teacher> findFirstByUsername(@Param("username") String username);

    @Query(value = "select * from teacher where user_id = :userId limit 1;", nativeQuery = true)
    Optional<Teacher> findFirstByUserId(@Param("userId") long userId);

    @Query(value = "select * from teacher join user_account on teacher.user_id = user_account.user_id where user_account.active = true and teacher.teacher_id not in (select teacher_id from class);", nativeQuery = true)
    List<Teacher> findAllWithoutClass();

    @Query(value = "select * from teacher where teacher_id in (select teacher_id from class)", nativeQuery = true)
    List<Teacher> findAllByWithClass();

    @Query(value = "select * from teacher where user_id = :userId limit 1;", nativeQuery = true)
    CompletableFuture<Teacher> findFirstByUserIdAsync(@Param("userId") long userId);
}
