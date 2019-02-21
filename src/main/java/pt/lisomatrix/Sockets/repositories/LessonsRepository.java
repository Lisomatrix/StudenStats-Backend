package pt.lisomatrix.Sockets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Lesson;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public interface LessonsRepository extends JpaRepository<Lesson, Long> {

    @Async
    CompletableFuture<Optional<Lesson>> findByLessonId(long lesson);

    Optional<Lesson> findFirstByClasseAndLessonId(Class a, Long lessonId);

    Optional<List<Lesson>> findAllByClasse(Class classs);

    Optional<List<Lesson>> findAllByClasseAndDiscipline(Class userClass, Discipline discipline);

    Optional<Lesson> findFirstByClasseAndDisciplineOrderByLessonNumberDesc(Class userClass, Discipline discipline);

    @Transactional
    @Modifying
    @Query(value = "UPDATE lesson SET summary = ?1 WHERE lesson_id = ?2", nativeQuery = true)
    void updateSummary(String summary, Long id);

}
