package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Parent;
import pt.lisomatrix.Sockets.models.User;

import java.util.Optional;

@Repository
public interface ParentsRepository extends JpaRepository<Parent, String> {

    Optional<Parent> findFirstByUser(User user);
}
