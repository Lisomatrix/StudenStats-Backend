package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Notification;
import pt.lisomatrix.Sockets.models.User;

import java.util.Optional;

@Repository
public interface NotificationsRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findAllByUser(User user);
}
