package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Storage;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
}
