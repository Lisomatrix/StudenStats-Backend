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

    Optional<Class> findFirstByStudents(Student student);

    @Query(value = "select * from class where class.teacher_id = (select teacher_id from teacher where user_id = (select user_id from user_account where email = :username)) limit 1;", nativeQuery = true)
    Optional<Class> findFirstByClassDirectorUsername(@Param("username") String username);

    @Query(value = "select * from class where class.teacher_id = (select teacher_id from teacher where user_id = :userId limit 1) limit 1;", nativeQuery = true)
    Optional<Class> findFirstByClassDirectorUserId(@Param("userId") long username);

    @Query(value = "select * from class where class.teacher_id = (select teacher_id from teacher where user_id = :userId) limit 1;", nativeQuery = true)
    Optional<Class> findFirstByTeacherUserId(@Param("userId") long userId);

    @Async
    @Query(value = "select * from class where class.teacher_id = (select teacher_id from teacher where user_id = :userId) limit 1;", nativeQuery = true)
    CompletableFuture<Optional<Class>> findFirstByTeacherUserIdAsync(@Param("userId") long userId);

    @Query(value = "select * from class where class_id = (select class_class_id from class_students where students_student_id = (select student_id from students where user_id = :userId limit 1) limit 1) limit 1;", nativeQuery = true)
    Optional<Class> findFirstByStudentUserId(@Param("userId") long userId);

    Optional<Class> findFirstByClassDirector(Teacher teacher);

    Optional<List<Class>> findAllByTeachers(Teacher teacher);

    Optional<List<Class>> findAllByTeachersIsIn(Teacher teacher);

    @Query(value = "select class.class_id, class.teacher_id, class.course_id, class.name , class.schedule_schedule_id from class left outer join class_teachers on class.class_id = class_teachers.class_class_id left outer join teacher on class_teachers.teachers_teacher_id = teacher.teacher_id where teacher.teacher_id in (select teacher_id from teacher where user_id = (select user_id from user_account where email = :username));", nativeQuery = true)
    Optional<List<Class>> findAllByTeachersUsernameIsIn(@Param("username") String username);

    @Query(value = "select class.class_id, class.teacher_id, class.course_id, class.name , class.schedule_schedule_id from class left outer join class_teachers on class.class_id = class_teachers.class_class_id left outer join teacher on class_teachers.teachers_teacher_id = teacher.teacher_id where teacher.teacher_id in (select teacher_id from teacher where user_id = :userId);", nativeQuery = true)
    Optional<List<Class>> findAllByTeachersUserIdIsIn(@Param("userId") long userId);

    @Async
    @Query(value = "select class.class_id, class.teacher_id, class.course_id, class.name , class.schedule_schedule_id from class left outer join class_teachers on class.class_id = class_teachers.class_class_id left outer join teacher on class_teachers.teachers_teacher_id = teacher.teacher_id where teacher.teacher_id in (select teacher_id from teacher where user_id = :userId);", nativeQuery = true)
    Callable<Optional<List<Class>>> findAllByTeachersUserIdIsInAsync(@Param("userId") long userId);
}
