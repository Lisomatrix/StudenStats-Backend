package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.File;

@Repository
public interface FilesRepository  extends JpaRepository<File, Long> {
}
