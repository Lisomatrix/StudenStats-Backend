package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Teacher;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplinesRepository extends JpaRepository<Discipline, Long> {

    Optional<List<Discipline>> findAllByTeachersIsIn(Teacher teacher);

}
