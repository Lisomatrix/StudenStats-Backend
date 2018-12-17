package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Hour;

@Repository
public interface HoursRepository extends JpaRepository<Hour, Long> {
}
