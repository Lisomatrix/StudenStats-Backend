package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.HourRecuperation;

@Repository
public interface HoursRecuperationRepository extends JpaRepository<HourRecuperation, Long> {
}
