package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.lisomatrix.Sockets.models.PasswordReset;

public interface PasswordResetsRepository extends JpaRepository<PasswordReset, String> {

}
