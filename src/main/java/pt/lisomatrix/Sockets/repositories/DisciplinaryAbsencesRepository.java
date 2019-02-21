package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.DisciplinaryAbsence;

@Repository
public interface DisciplinaryAbsencesRepository extends JpaRepository<DisciplinaryAbsence, Long> {

}
