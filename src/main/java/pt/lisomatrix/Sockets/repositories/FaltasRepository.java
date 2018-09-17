package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Falta;

@Repository
public interface FaltasRepository extends JpaRepository<Falta, Long> {
}
