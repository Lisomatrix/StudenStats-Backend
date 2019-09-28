package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TestsRepository extends JpaRepository<Test, Long> {

    Optional<List<Test>> findAllByTeacherAndDateAfter(Teacher teacher, Date date);

    Optional<List<Test>> findAllByTestClassAndDateAfter(Class userClass, Date date);

    Optional<List<Test>> findAllByTestClass(Class userClass);

    @Query(value = "select * from test where test_id = :testId and teacher_id = (select teacher_id from teacher where user_id = :userId)", nativeQuery = true)
    Optional<Test> findFirstByTestIdAndTeacherId(@Param("userId") long userId, @Param("testId") long testId);

}
