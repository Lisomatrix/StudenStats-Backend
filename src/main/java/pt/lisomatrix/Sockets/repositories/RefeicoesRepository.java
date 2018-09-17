package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Refeicao;

@Repository
public interface RefeicoesRepository extends JpaRepository<Refeicao, Long> {
}
