package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    Optional<User> findFirstByUsernameAndActive(String email, boolean active);

    Optional<User> findByRegistrationCodeAndActive(String registrationCode, boolean active);

    @Query(value = "select * from user_account where active = true and user_id in (select user_id from user_settings where user_id in (select user_id from parent where parent_id in (select parent_parent_id from parent_childs where childs_student_id in (select students_student_id from class_students where class_class_id = :classId ))) and allow_emails = true);", nativeQuery = true)
    Optional<List<User>> findAllByClassId(@Param("classId") long classId);

    @Query(value = "SELECT * FROM user_account where active = true;", nativeQuery = true)
    List<User> findAllActiveUsers();
}
