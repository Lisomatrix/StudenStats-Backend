package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentsRepository extends JpaRepository<Student, Long> {

    Optional<Student> findFirstByUser(User user);

    @Query(value = "SELECT * FROM students WHERE student_id IN (SELECT students_student_id FROM class_students WHERE class_class_id = :id)", nativeQuery = true)
    Optional<List<Student>> findAllByClassId(@Param("id") long ClassId);

    @Query(value = "select * from students where user_id = (select user_id from user_account where email = :username);", nativeQuery = true)
    Optional<Student> findFirstByUsername(@Param("username") String username);

    @Query(value = "select * from students where user_id = :userId ;", nativeQuery = true)
    Optional<Student> findFirstByUserId(@Param("userId") long userId);

    @Query(value = "select * from students where student_id in (select children_student_id from parent_childs where parent_parent_id = (select parent_id from parent where user_id = :userId limit 1));", nativeQuery = true)
    Optional<List<Student>> findAllByParentUserId(@Param("userId") long userId);

    @Query(value = "select * from students join user_account on students.user_id = user_account.user_id where students.student_id not in (select students_student_id from class_students) and user_account.active = true;", nativeQuery = true)
    Optional<List<Student>> findAllStudentsWithoutClass();

    @Query(value = "select * from students where student_id not in (select children_student_id from parent_childs) and user_id not in (select user_id from user_account where active = false);", nativeQuery = true)
    List<Student> findAllWithoutParent();
}
