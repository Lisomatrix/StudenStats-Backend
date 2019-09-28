package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Admin;
import pt.lisomatrix.Sockets.models.User;

import java.util.Optional;

@Repository
public interface AdminsRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findFirstByUser(User user);
}
