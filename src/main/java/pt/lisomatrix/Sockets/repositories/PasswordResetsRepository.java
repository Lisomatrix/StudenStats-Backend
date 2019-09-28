package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.PasswordReset;

@Repository
public interface PasswordResetsRepository extends JpaRepository<PasswordReset, Long> {

}
