package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface AbsencesRepository extends JpaRepository<Absence, Long> {

    Optional<List<Absence>> findAllByStudent(Student student);

    Optional<List<Absence>> findAllByLesson(Lesson lesson);

    @Transactional
    Optional<List<Absence>> deleteAbsenceByStudentAndDisciplineAndAbsenceType(Student student, Discipline discipline, AbsenceType absenceType);

    Optional<List<Absence>> findAllByStudentIn(List<Student> students);

    @Query( value = "select * from absence o where student_id in :ids", nativeQuery = true)
    Optional<List<Absence>> findAllByStudentId(@Param("ids") String[] studentIds);
    
}
