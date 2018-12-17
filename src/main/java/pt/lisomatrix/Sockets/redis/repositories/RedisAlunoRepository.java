package pt.lisomatrix.Sockets.redis.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.redis.models.RedisAluno;

@Repository
public interface RedisAlunoRepository extends CrudRepository<RedisAluno, Long> {


}
