package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Module;
import pt.lisomatrix.Sockets.models.ModuleGrade;
import pt.lisomatrix.Sockets.models.Student;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleGradesRepository  extends JpaRepository<ModuleGrade, Long> {

    @Query(value = "select * from module_grade where student_id = :studentId ;", nativeQuery = true)
    Optional<List<ModuleGrade>> findAllByStudentId(@Param("studentId") long studentId);

    Optional<List<ModuleGrade>> findAllByModule(Module module);

    @Query(value = "select * from module_grade where student_id in (select students_student_id from class_students where class_class_id = :id)", nativeQuery = true)
    Optional<List<ModuleGrade>> findAllByClass(@Param("id") long classId);

    @Query(value = "select * from module_grade where module_id = :moduleId and student_id in ( :studentIds );", nativeQuery = true)
    Optional<List<ModuleGrade>> findAllByModuleIdAndStudentIds(@Param("moduleId") long moduleId, @Param("studentIds") List<Long> studentIds);

    @Transactional
    @Modifying
    @Query(value = "update module_grade set grade = :grade where module_grade_id = :moduleGradeId ;", nativeQuery = true)
    void updateExistingModuleGrades(@Param("grade") int grade, @Param("moduleGradeId") long moduleGradeId);
}
