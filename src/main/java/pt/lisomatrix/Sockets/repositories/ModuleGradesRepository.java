package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Module;
import pt.lisomatrix.Sockets.models.ModuleGrade;
import pt.lisomatrix.Sockets.models.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleGradesRepository  extends JpaRepository<ModuleGrade, Long> {

    Optional<List<ModuleGrade>> findAllByStudent(Student student);

    Optional<List<ModuleGrade>> findAllByModule(Module module);

    @Query( value = "select * from module_grade where student_id in (select students_student_id from class_students where class_class_id = :id)", nativeQuery = true)
    Optional<List<ModuleGrade>> findAllByClass(@Param("id") long classId);
}
