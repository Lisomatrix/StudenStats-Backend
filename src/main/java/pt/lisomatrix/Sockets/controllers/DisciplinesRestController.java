package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.models.User;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.DisciplinesRepository;
import pt.lisomatrix.Sockets.repositories.TeachersRepository;
import pt.lisomatrix.Sockets.repositories.UsersRepository;
import pt.lisomatrix.Sockets.requests.models.Response;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
public class DisciplinesRestController {

    @Autowired
    private DisciplinesRepository disciplinesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TeachersRepository teachersRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @GetMapping("/class/{classId}/discipline")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<Discipline> getClassDisciplines(@PathVariable("classId") long classId, Principal principal) {

        Optional<List<Discipline>> foundDisciplines = disciplinesRepository.findAllByClass(classId);

        if(foundDisciplines.isPresent()) {

            return foundDisciplines.get();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Class not found");
    }

    @GetMapping("/class/{classId}/discipline/async")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public DeferredResult<ResponseEntity<?>> getClassDisciplinesAsync(@PathVariable("classId") long classId, Principal principal) {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult();

        CompletableFuture.supplyAsync(() -> disciplinesRepository.findAllByClass(classId))
                .thenApplyAsync((foundDisciplines) -> {
                    if(foundDisciplines.isPresent()) {

                        deferredResult.setResult(ResponseEntity.ok(foundDisciplines.get()));

                    } else {
                        deferredResult.setResult(ResponseEntity.badRequest().build());
                    }

                    return null;
                });

        return deferredResult;
    }

    @GetMapping("/teacher/discipline")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<Discipline> getTeacherDisciplines(Principal principal) {

        Optional<List<Discipline>> foundDiscipline = disciplinesRepository.findAllByTeacherUserIdIsIn(Long.parseLong(principal.getName()));

        if(foundDiscipline.isPresent()) {

            List<Discipline> disciplines = foundDiscipline.get();

            return disciplines;
        } else {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Disciplines not found");
        }
    }

    @GetMapping("/teacher/discipline/async")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public DeferredResult<ResponseEntity<?>> getTeacherDisciplinesAsync(Principal principal) {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult();

        CompletableFuture.supplyAsync(() -> disciplinesRepository.findAllByTeacherUserIdIsIn(Long.parseLong(principal.getName())))
                .thenApplyAsync((foundDisciplines) -> {

                    if(foundDisciplines.isPresent()) {

                        deferredResult.setResult(ResponseEntity.ok(foundDisciplines.get()));

                    } else {
                        deferredResult.setResult(ResponseEntity.badRequest().build());
                    }

                    return null;
                });

        return deferredResult;
    }
}
