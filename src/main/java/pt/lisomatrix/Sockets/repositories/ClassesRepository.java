package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.models.Teacher;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassesRepository extends JpaRepository<Class, Long> {

    Optional<Class> findFirstByStudents(Student student);

    Optional<Class> findFirstByClassDirector(Teacher teacher);

    Optional<List<Class>> findAllByTeachers(Teacher teacher);

    Optional<List<Class>> findAllByTeachersIsIn(Teacher teacher);
}
