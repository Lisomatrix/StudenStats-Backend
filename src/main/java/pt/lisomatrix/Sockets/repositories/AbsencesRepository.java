package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.concurrent.ListenableFuture;
import pt.lisomatrix.Sockets.models.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@Repository
public interface AbsencesRepository extends JpaRepository<Absence, Long> {

    Optional<List<Absence>> findAllByStudent(Student student);

    @Query(value = "select * from absence where student_id = (select student_id from students where user_id = (select user_id from user_account where email = :username));", nativeQuery = true)
    Optional<List<Absence>> findAllByStudentUsername(@Param("username") String username);

    @Query(value = "select * from absence where student_id = (select student_id from students where user_id = :userId limit 1);", nativeQuery = true)
    Optional<List<Absence>> findAllByStudentUserId(@Param("userId") long username);

    Optional<List<Absence>> findAllByLesson(Lesson lesson);

    @Transactional
    Optional<List<Absence>> deleteAbsenceByStudentAndDisciplineAndAbsenceType(Student student, Discipline discipline, AbsenceType absenceType);

    @Query(value = "select * from absence where student_id in (select student_id from parent_childs where parent_parent_id = (select parent_id from parent where user_id = :userId limit 1));", nativeQuery = true)
    Optional<List<Absence>> findAllByParentUserId(@Param("userId") long userId);

    @Query( value = "select * from absence o where student_id in :ids", nativeQuery = true)
    Optional<List<Absence>> findAllByStudentId(@Param("ids") Long[] studentIds);

    @Query( value = "select * from absence o where student_id in :ids", nativeQuery = true)
    CompletableFuture<Optional<List<Absence>>> findAllByStudentIdAsync(@Param("ids") Long[] studentIds);
}
