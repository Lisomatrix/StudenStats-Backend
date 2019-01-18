package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Grade;
import pt.lisomatrix.Sockets.models.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradesRepository extends JpaRepository<Grade, Long> {

    Optional<List<Grade>> findAllByStudentIsIn(Student student);

    @Query( value = "select * from grade o where student_id in :ids", nativeQuery = true)
    Optional<List<Grade>> findAllByStudentId(@Param("ids") Long[] studentIds);
}
