package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Hour;

@Repository
public interface SchedulesRepository extends JpaRepository<Hour, Long> {
}
