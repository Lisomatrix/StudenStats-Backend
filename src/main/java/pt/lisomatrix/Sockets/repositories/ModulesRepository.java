package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Module;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModulesRepository extends JpaRepository<Module, Long> {

    Optional<List<Module>> findAllByDiscipline(Discipline discipline);

    @Query(value = "select max(module_id) as module_id from lesson where discipline_id = :disciplineId and class_id = :classId", nativeQuery = true)
    Optional<Module> findLastModule(@Param("disciplineId") long disciplineId, @Param("classId") long classId);
}
