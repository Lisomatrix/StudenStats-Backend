package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.File;
import pt.lisomatrix.Sockets.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilesRepository  extends JpaRepository<File, String> {

    @Query(value = "select sum(file_size) from files where user_id = :userId", nativeQuery = true)
    Long getUsedSpace(@Param("userId") long userId);

    @Query(value = "select * from files where user_id = :userId", nativeQuery = true)
    Optional<List<File>> findUserFiles(@Param("userId") long userId);

    @Query(value = "select * from files where storage_number = :storageNumber ;", nativeQuery = true)
    Optional<List<File>> findAllByStorageNumber(@Param("storageNumber") int storageNumber);
}
