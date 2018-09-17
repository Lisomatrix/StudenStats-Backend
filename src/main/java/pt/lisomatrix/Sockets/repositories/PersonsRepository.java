package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.lisomatrix.Sockets.models.Person;

import java.util.Optional;

public interface PersonsRepository extends JpaRepository<Person, Long> {


}
