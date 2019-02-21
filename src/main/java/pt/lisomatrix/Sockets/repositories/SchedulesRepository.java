package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Hour;
import pt.lisomatrix.Sockets.models.Schedule;

import java.util.Optional;

@Repository
public interface SchedulesRepository extends JpaRepository<Schedule, Long> {

    @Query( value = "SELECT * FROM schedule WHERE schedule_id = ( SELECT schedule_id from teacher WHERE teacher_id = :id LIMIT 1) LIMIT 1", nativeQuery = true)
    Optional<Schedule> findFirstByTeacher(@Param("id") Long teacherId);

    @Query( value = "SELECT * FROM schedule WHERE schedule_id = ( SELECT schedule_id from class WHERE class_id = :id LIMIT 1) LIMIT 1", nativeQuery = true)
    Optional<Schedule> findFirstByClass(@Param("id") long teacherId);
}
