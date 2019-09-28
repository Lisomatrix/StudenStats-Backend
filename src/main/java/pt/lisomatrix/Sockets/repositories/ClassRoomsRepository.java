package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.ClassRoom;

import java.util.Optional;

@Repository
public interface ClassRoomsRepository extends JpaRepository<ClassRoom, Long> {
}
