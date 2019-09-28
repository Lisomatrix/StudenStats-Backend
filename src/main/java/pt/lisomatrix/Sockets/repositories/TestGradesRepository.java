package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.models.Test;
import pt.lisomatrix.Sockets.models.TestGrade;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestGradesRepository extends JpaRepository<TestGrade, Long> {

    Optional<List<TestGrade>> findAllByTest(Test test);

    @Query(value = "select * from test_grade where student_id in (:studentId) and test_id = :testId ;", nativeQuery = true)
    Optional<List<TestGrade>> findAllByStudentIdAndTestId(@Param("studentId") long[] studentId, @Param("testId") long testId);

    @Query(value = "select * from test_grade where test_id in (select test_id from test where module_id = :moduleId and class_id = :classId );", nativeQuery = true)
    Optional<List<TestGrade>> findAllByClassAndModule(@Param("classId") long classId, @Param("moduleId") long moduleId);

    Optional<List<TestGrade>> findAllByStudent(Student student);

    @Transactional
    @Modifying
    @Query(value = "update test_grade set grade = :grade where test_grade_id = :testGradeId ;", nativeQuery = true)
    void updateExistingTestGrades(@Param("grade") int grade, @Param("testGradeId") long testGradeId);
}
