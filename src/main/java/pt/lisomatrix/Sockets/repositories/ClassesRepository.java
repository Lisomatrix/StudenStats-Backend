package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.util.concurrent.ListenableFuture;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.models.Teacher;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Repository
public interface ClassesRepository extends JpaRepository<Class, Long> {

    @Query(value = "select * from class where active = true and class.teacher_id = (select teacher_id from teacher where user_id = :userId limit 1) limit 1;", nativeQuery = true)
    Optional<Class> findFirstByClassDirectorUserId(@Param("userId") long username);

    @Query(value = "select * from class where active = true and class.teacher_id = (select teacher_id from teacher where user_id = :userId) limit 1;", nativeQuery = true)
    Optional<Class> findFirstByTeacherUserId(@Param("userId") long userId);

    Optional<Class> findFirstByClassDirector(Teacher teacher);

    @Query(value = "select class.active, class.year, class.class_id, class.teacher_id, class.course_id, class.name from class left outer join class_teachers on class.class_id = class_teachers.class_class_id left outer join teacher on class_teachers.teachers_teacher_id = teacher.teacher_id where teacher.teacher_id in (select teacher_id from teacher where active = true and user_id = :userId);", nativeQuery = true)
    Optional<List<Class>> findAllByTeachersUserIdIsIn(@Param("userId") long userId);

    List<Class> findAllByActive(boolean active);

    @Query(value = "select * from class where active = true and class_id = (select class_class_id from class_teachers where class_class_id = :classId and teachers_teacher_id = (select teacher_id from teacher where user_id = :userId limit 1) limit 1) limit 1;", nativeQuery = true)
    Optional<Class> findFirstByClassAndTeacher(@Param("classId") long classId, @Param("userId") long userId);

    Optional<Class> findFirstByStudentsIn(Student student);

    @Query(value = "select * from class where active = true and class_id = (select class_class_id from class_students where students_student_id = (select children_student_id from parent_childs where parent_parent_id = (select parent_id from parent where user_id = :userId limit 1) and children_student_id = :studentId limit 1) limit 1) limit 1;", nativeQuery = true)
    Optional<Class> findFirstByParentUserIdAndStudentId(@Param("userId") long userId, @Param("studentId") long studentId);
}
