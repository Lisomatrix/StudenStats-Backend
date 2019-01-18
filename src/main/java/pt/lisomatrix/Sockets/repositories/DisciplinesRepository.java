package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Teacher;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplinesRepository extends JpaRepository<Discipline, Long> {

    Optional<List<Discipline>> findAllByTeachersIsIn(Teacher teacher);

    @Query( value = "select * from discipline where discipline_id in (select disciplines_discipline_id from class_disciplines where class_class_id = :id)", nativeQuery = true)
    Optional<List<Discipline>> findAllByClass(@Param("id") long classId);

}
