package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.AbsenceType;

import java.util.Optional;

@Repository
public interface AbsenceTypesRepository extends JpaRepository<AbsenceType, Long> {

    Optional<AbsenceType> findFirstByName(String name);
}
