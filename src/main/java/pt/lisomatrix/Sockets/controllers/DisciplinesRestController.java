package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
import pt.lisomatrix.Sockets.requests.models.NewDiscipline;
import pt.lisomatrix.Sockets.requests.models.Response;

import java.security.Principal;
import java.util.ArrayList;
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

    @GetMapping("/discipline")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public List<Discipline> getDisciplines() {
        return disciplinesRepository.findAll();
    }

    @PostMapping("/discipline")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public Discipline addDiscipline(@RequestBody NewDiscipline newDiscipline) {

        Discipline discipline = new Discipline();

        discipline.setName(newDiscipline.getName());
        discipline.setAbbreviation(newDiscipline.getAbbreviation());

        discipline = disciplinesRepository.save(discipline);

        return discipline;
    }

    @PutMapping("/discipline/{disciplineId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public Discipline updateDiscipline(@PathVariable long disciplineId, @RequestBody NewDiscipline newDiscipline) {

        Optional<Discipline> foundDiscipline = disciplinesRepository.findById(disciplineId);

        if(foundDiscipline.isPresent()) {

            Discipline discipline = foundDiscipline.get();

            discipline.setAbbreviation(newDiscipline.getAbbreviation());
            discipline.setName(newDiscipline.getName());

            return disciplinesRepository.save(discipline);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found!");
    }

    @PostMapping("/user/{teacherUserId}/discipline/{disciplineId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ResponseEntity<?> addTeacherDiscipline(@PathVariable long teacherUserId, @PathVariable long disciplineId) {

        Optional<Discipline> foundDiscipline = disciplinesRepository.findById(disciplineId);

        Optional<Teacher> foundTeacher = teachersRepository.findFirstByUserId(teacherUserId);

        if(foundDiscipline.isPresent()) {
            if(foundTeacher.isPresent()) {

                Teacher teacher = foundTeacher.get();

                Discipline discipline = foundDiscipline.get();

                List<Teacher> teachers = discipline.getTeachers();

                boolean alreadyAdded = false;

                for(int i = 0; i < teachers.size(); i++) {
                    if(teachers.get(i).getTeacherId().equals(teacher.getTeacherId())) {
                        alreadyAdded = true;
                        break;
                    }
                }

                if(!alreadyAdded) {
                    teachers.add(teacher);

                    discipline.setTeachers(teachers);

                    disciplinesRepository.save(discipline);

                    return ResponseEntity.ok(discipline);
                }

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already added!");
            }

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found!");
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found!");
    }

    @DeleteMapping("/user/{teacherUserId}/discipline/{disciplineId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ResponseEntity<?> removeTeacherDiscipline(@PathVariable long teacherUserId, @PathVariable long disciplineId) {

        Optional<Discipline> foundDiscipline = disciplinesRepository.findById(disciplineId);

        Optional<Teacher> foundTeacher = teachersRepository.findFirstByUserId(teacherUserId);

        if(foundDiscipline.isPresent()) {
            if(foundTeacher.isPresent()) {

                Teacher teacher = foundTeacher.get();

                Discipline discipline = foundDiscipline.get();

                List<Teacher> teachers = discipline.getTeachers();

                teachers.remove(teacher);

                discipline.setTeachers(teachers);

                disciplinesRepository.save(discipline);

                return ResponseEntity.ok().build();
            }

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found!");
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found!");
    }

    @GetMapping("/class/{classId}/discipline")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasRole('ROLE_ALUNO') or hasRole('ROLE_PARENT')")
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

    @GetMapping("/user/{teacherUserId}/discipline")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public List<Discipline> getAdminTeacherDisciplines(@PathVariable long teacherUserId) {
        Optional<Teacher> foundTeacher = teachersRepository.findFirstByUserId(teacherUserId);

        if(foundTeacher.isPresent()) {

            Optional<List<Discipline>> foundDiscipline = disciplinesRepository.findAllByTeacherUserIdIsIn(teacherUserId);

            if(foundDiscipline.isPresent()) {

                List<Discipline> disciplines = foundDiscipline.get();

                return disciplines;
            } else {
                return new ArrayList<>();
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found");
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
            return new ArrayList<>();
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
