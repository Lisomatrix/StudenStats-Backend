package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Parent;
import pt.lisomatrix.Sockets.models.User;

import java.util.Optional;

@Repository
public interface ParentsRepository extends JpaRepository<Parent, String> {

    Optional<Parent> findFirstByUser(User user);

    @Query(value = "select * from parent where user_id = :userId limit 1;", nativeQuery = true)
    Optional<Parent> findFirstByUserId(@Param("userId") long userId);

    @Query(value = "select * from parent where parent_id = (select parent_parent_id from parent_childs where children_student_id = (select student_id from students where user_id = :userId limit 1) limit 1) limit 1;", nativeQuery = true)
    Optional<Parent> findFirstByStudentUserId(@Param("userId") long userId);

    @Query(value = "select * from parent where parent_id = (select parent_parent_id from parent_childs where children_student_id = :studentId limit 1) limit 1;", nativeQuery = true)
    Optional<Parent> findFirstByStudentId(@Param("studentId") long studentId);
}
