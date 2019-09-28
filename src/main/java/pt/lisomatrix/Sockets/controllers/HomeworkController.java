package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Homework;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.DisciplinesRepository;
import pt.lisomatrix.Sockets.repositories.HomeworksRepository;
import pt.lisomatrix.Sockets.repositories.TeachersRepository;
import pt.lisomatrix.Sockets.requests.models.MarkHomework;
import pt.lisomatrix.Sockets.response.models.HomeworkResponse;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class HomeworkController {

    @Autowired
    private HomeworksRepository homeworksRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private TeachersRepository teachersRepository;

    @Autowired
    private DisciplinesRepository disciplinesRepository;

    @GetMapping("/class/{classId}/homework")
    @CrossOrigin
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasAnyRole('ROLE_ALUNO')")
    public ResponseEntity<?> getHomework(@PathVariable("classId") Long classId) {

        Class aClass = new Class();

        aClass.setClassId(classId);

        Optional<List<Homework>> foundHomeWorks = homeworksRepository.findAllByAClassAndExpireDateAfter(aClass, new Date());

        if(foundHomeWorks.isPresent()) {

            List<HomeworkResponse> homeworkResponses = populateHomeworkDAOList(foundHomeWorks.get());


            return ResponseEntity.ok(homeworkResponses);
        }

        return ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping("/class/{classId}/homework")
    @CrossOrigin
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public ResponseEntity<?> markHomework(@PathVariable("classId") Long classId, @RequestBody MarkHomework markHomework, Principal principal) {

        if(markHomework.getDate().compareTo(new Date()) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "That date has already passed");
        }


        Teacher teacher = teachersRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

        Optional<Class> foundClass = classesRepository.findFirstByClassAndTeacher(classId, Long.parseLong(principal.getName()));

        if(foundClass.isPresent()) {

            Optional<Discipline> foundDiscipline = disciplinesRepository.findDisciplineByIdAndTeacher(markHomework.getDisciplineId(), teacher.getTeacherId());

            if(!foundDiscipline.isPresent()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Discipline not found or not allowed");
            }

            Homework homework = new Homework();

            homework.setTeacher(teacher);
            homework.setaClass(foundClass.get());
            homework.setDescription(markHomework.getDescription());
            homework.setExpireDate(markHomework.getDate());
            homework.setTitle(markHomework.getTitle());
            homework.setDiscipline(foundDiscipline.get());

            homework = homeworksRepository.save(homework);

            HomeworkResponse homeworkResponse = new HomeworkResponse();

            homeworkResponse.populate(homework, classId);

            return ResponseEntity.ok(homeworkResponse);
        }

        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }

    @GetMapping("/teacher/{teacherId}/homework")
    @CrossOrigin
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public ResponseEntity<?> getTeacherHomeworks(@PathVariable("teacherId") Long teacherId, Principal principal) {

        Teacher teacher = teachersRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

        if(teacher.getTeacherId().equals(teacherId)) {

            Optional<List<Homework>> foundHomeworks = homeworksRepository.findAllByTeacher(teacher);

            if(foundHomeworks.isPresent()) {

                List<HomeworkResponse> homeworkResponses = populateHomeworkDAOList(foundHomeworks.get());

                return ResponseEntity.ok(homeworkResponses);

            } else {
                return ResponseEntity.ok(new ArrayList<>());
            }

        }

        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }

    private List<HomeworkResponse> populateHomeworkDAOList(List<Homework> homework) {

        List<HomeworkResponse> homeworkResponses = new ArrayList<>();

        for(int i = 0; i < homework.size(); i++) {

            HomeworkResponse homeworkResponse = new HomeworkResponse();
            homeworkResponse.populate(homework.get(i), homework.get(i).getaClass().getClassId());

            homeworkResponses.add(homeworkResponse);
        }

        return homeworkResponses;
    }
}
