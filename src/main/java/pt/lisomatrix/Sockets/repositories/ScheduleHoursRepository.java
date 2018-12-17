package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.ScheduleHour;

@Repository
public interface ScheduleHoursRepository extends JpaRepository<ScheduleHour, Long> {
}
