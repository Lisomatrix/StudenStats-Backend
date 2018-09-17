package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Aluno;

import java.util.Optional;

@Repository
public interface AlunosRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByRegistrationCode(String registrationCode);

    Optional<Aluno> findByEmail(String email);
}
