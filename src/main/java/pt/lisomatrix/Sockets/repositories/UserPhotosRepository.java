package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.User;
import pt.lisomatrix.Sockets.models.UserPhoto;

import java.util.Optional;

@Repository
public interface UserPhotosRepository extends JpaRepository<UserPhoto, String> {

    Optional<UserPhoto> findFirstByUser(User user);

    @Query(value = "select * from user_profile_photos where user_id = :userId limit 1;", nativeQuery = true)
    Optional<UserPhoto> findFirstByUserId(@Param("userId") String userId);

    @Query(value = "select * from user_profile_photos where user_id = (select user_id from students where student_id = :studentId limit 1) limit 1;", nativeQuery = true)
    Optional<UserPhoto> findFirstByStudentId(@Param("studentId") String studentId);

}
