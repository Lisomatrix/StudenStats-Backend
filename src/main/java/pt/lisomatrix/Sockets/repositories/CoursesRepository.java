package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Course;

@Repository
public interface CoursesRepository extends JpaRepository<Course, Long> {
}
