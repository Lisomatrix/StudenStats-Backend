package pt.lisomatrix.Sockets.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.models.User;

import java.util.Optional;

@Repository
public interface TeachersRepository extends JpaRepository<Teacher, String> {

    Optional<Teacher> findFirstByUser(User user);
}
