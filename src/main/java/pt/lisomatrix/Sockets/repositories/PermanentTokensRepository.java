package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.PermanentToken;

import java.util.Optional;

@Repository
public interface PermanentTokensRepository  extends JpaRepository<PermanentToken, Long> {

    Optional<PermanentToken> findFirstByPermanentToken(String token);
}
