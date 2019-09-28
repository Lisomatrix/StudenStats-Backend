package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
