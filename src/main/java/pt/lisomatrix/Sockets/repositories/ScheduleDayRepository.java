package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.ScheduleDay;

@Repository
public interface ScheduleDayRepository extends JpaRepository<ScheduleDay, Long> {
}
