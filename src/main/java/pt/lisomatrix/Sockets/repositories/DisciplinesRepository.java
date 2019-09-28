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

    @Query(value = "select * from discipline where discipline_id in (select disciplines_discipline_id from teacher_disciplines where teacher_teacher_id = (select teacher_teacher_id from teacher where user_id = (select user_id from user_account where email = :username)));", nativeQuery = true)
    Optional<List<Discipline>> findAllByTeacherUsernameIsIn(@Param("username") String username);

    @Query(value = "select * from discipline where discipline_id in (select discipline_discipline_id from discipline_teachers where teachers_teacher_id = (select teacher_id from teacher where user_id = :userId ));", nativeQuery = true)
    Optional<List<Discipline>> findAllByTeacherUserIdIsIn(@Param("userId") long userId);

    Optional<List<Discipline>> findAllByTeachersIsIn(Teacher teacher);

    @Query(value = "select * from discipline where discipline_id in (select disciplines_discipline_id from class_disciplines where class_class_id = :id)", nativeQuery = true)
    Optional<List<Discipline>> findAllByClass(@Param("id") long classId);

    @Query(value = "select * from discipline where discipline_id = (select discipline_discipline_id from discipline_teachers where teachers_teacher_id = :teacherId and discipline_discipline_id = :disciplineId limit 1) limit 1;", nativeQuery = true)
    Optional<Discipline> findDisciplineByIdAndTeacher(@Param("disciplineId") long disciplineId, @Param("teacherId") long teacherId);
}
