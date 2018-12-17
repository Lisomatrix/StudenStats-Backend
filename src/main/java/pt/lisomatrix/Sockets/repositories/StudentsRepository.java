package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentsRepository extends JpaRepository<Student, String> {

    Optional<Student> findFirstByUser(User user);

    @Query(value = "SELECT * FROM students WHERE student_id IN (SELECT students_student_id FROM class_students WHERE class_class_id = :id)", nativeQuery = true)
    Optional<List<Student>> findAllByClassId(@Param("id") long ClassId);
}