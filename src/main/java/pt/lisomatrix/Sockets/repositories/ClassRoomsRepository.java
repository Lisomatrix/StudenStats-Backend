package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.ClassRoom;

@Repository
public interface ClassRoomsRepository extends JpaRepository<ClassRoom, Long> {
}
