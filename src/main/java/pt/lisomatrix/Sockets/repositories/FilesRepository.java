package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.File;
import pt.lisomatrix.Sockets.models.User;

@Repository
public interface FilesRepository  extends JpaRepository<File, String> {

    @Query(value = "select sum(file_size) from files where user_id = :userId", nativeQuery = true)
    Long getUsedSpace(@Param("userId") long userId);
}
