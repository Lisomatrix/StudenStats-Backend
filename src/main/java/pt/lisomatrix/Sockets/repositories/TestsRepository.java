package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
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

}
