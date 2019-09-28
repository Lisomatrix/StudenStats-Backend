package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Homework;
import pt.lisomatrix.Sockets.models.Teacher;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface HomeworksRepository extends JpaRepository<Homework, Long> {

    @Query(value = "select * from homework where class_id = :classId ;", nativeQuery = true)
    Optional<List<Homework>> findAllByaClass(@Param("classId") long classId);

    Optional<List<Homework>> findAllByAClassAndExpireDateAfter(Class aClass, Date date);

    Optional<List<Homework>> findAllByTeacher(Teacher teacher);
}
