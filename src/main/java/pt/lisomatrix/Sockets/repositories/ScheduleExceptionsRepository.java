package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.ScheduleException;

@Repository
public interface ScheduleExceptionsRepository extends JpaRepository<ScheduleException, Long> {
}
