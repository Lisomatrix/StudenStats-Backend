package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Lesson;
import pt.lisomatrix.Sockets.models.Module;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.repositories.*;
import pt.lisomatrix.Sockets.requests.models.NewLesson;
import pt.lisomatrix.Sockets.requests.models.UpdateLessonSummary;
import pt.lisomatrix.Sockets.websocket.models.Event;
import pt.lisomatrix.Sockets.websocket.models.LessonDAO;
import pt.lisomatrix.Sockets.websocket.models.ModuleDAO;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class LessonRestController {

    @Autowired
    private LessonsRepository lessonsRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private DisciplinesRepository disciplinesRepository;

    @Autowired
    private ModulesRepository modulesRepository;

    @Autowired
    private TeachersRepository teachersRepository;

    @CrossOrigin
    @PutMapping("/lesson")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Lesson updateLessonSummary(@RequestBody UpdateLessonSummary updateLessonSummary, Principal principal) {

        Optional<Lesson> foundLesson = lessonsRepository.findById(updateLessonSummary.getLessonId());

        if(foundLesson.isPresent()) {

            Lesson lesson = foundLesson.get();

            Teacher teacher = teachersRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

            if(lesson.getTeacher().getTeacherId().equals(teacher.getTeacherId())) {

                lesson.setSummary(updateLessonSummary.getSummary());

                Lesson updatedLesson = lessonsRepository.save(lesson);

                return updatedLesson;
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Lesson not found");
    }

    @CrossOrigin
    @PostMapping("/lesson")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public LessonDAO newLesson(@RequestBody  NewLesson newLesson, Principal principal) {

        Teacher teacher = teachersRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

        if(newLesson.getTeacherId() == teacher.getTeacherId()) {

            Date currentDate = new Date();

            Lesson lesson = new Lesson();

            Optional<Discipline> foundDiscipline = disciplinesRepository.findById(newLesson.getDisciplineId());

            if(!foundDiscipline.isPresent()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Discipline not found");
            }

            lesson.setClasse(new Class(newLesson.getClassId()));
            lesson.setDiscipline(foundDiscipline.get());
            lesson.setTeacher(teacher);
            lesson.setDate(currentDate);

            Optional<List<pt.lisomatrix.Sockets.models.Module>> foundModules = modulesRepository.findAllByDiscipline(foundDiscipline.get());

            if(!foundModules.isPresent()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Module not found");
            }

            List<pt.lisomatrix.Sockets.models.Module> modules = foundModules.get();

            Optional<Lesson> foundLesson = lessonsRepository.findFirstByClasseAndDisciplineOrderByLessonNumberDesc(new Class(newLesson.getClassId()), new Discipline(newLesson.getDisciplineId()));

            if(foundLesson.isPresent()) {

                Lesson lastLesson = foundLesson.get();

                final int lastLessonNumber = lastLesson.getLessonNumber() + 1;

                lesson.setLessonNumber(lastLessonNumber);

                pt.lisomatrix.Sockets.models.Module currentModule = null;

                for(int i = 0; i < modules.size(); i++) {
                    if(modules.get(i).getHours() >  lastLessonNumber) {
                        if(modules.size() < i+1) {
                            currentModule = modules.get(i);
                            break;
                        } else if(modules.size() > i+1 && modules.get(i+1).getHours() < lastLessonNumber) {
                            currentModule = modules.get(i);
                            break;
                        }
                    }
                }

                if(currentModule == null) {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Current module not found");
                }

                lesson.setModule(currentModule);

                Lesson createdLesson = lessonsRepository.save(lesson);

                ModuleDAO moduleDAO = new ModuleDAO();

                moduleDAO.populate(currentModule);

                LessonDAO createdLessonDAO = new LessonDAO();

                createdLessonDAO.setLessonId(createdLesson.getLessonId());
                createdLessonDAO.setDisciplineId(createdLesson.getDiscipline().getDisciplineId());
                createdLessonDAO.setDate(createdLesson.getDate());
                createdLessonDAO.setClassId(createdLesson.getClasse().getClassId());
                createdLessonDAO.setLessonNumber(createdLesson.getLessonNumber());
                createdLessonDAO.setModule(moduleDAO);

                return createdLessonDAO;

            } else {

                Module currentModule = null;

                for(int i = 0; i < modules.size(); i++) {
                    if(modules.get(i).getHours() >  1) {
                        if(modules.size() < i+1) {
                            currentModule = modules.get(i);
                            break;
                        } else if(modules.size() > i+1 && modules.get(i+1).getHours() < 1) {
                            currentModule = modules.get(i);
                            break;
                        }
                    }
                }

                if(currentModule == null) {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Current module not found");
                }

                lesson.setLessonNumber(1);
                lesson.setModule(currentModule);

                // Insert object to database
                Lesson createdLesson = lessonsRepository.save(lesson);

                LessonDAO createdLessonDAO = new LessonDAO();

                createdLessonDAO.setLessonId(createdLesson.getLessonId());
                createdLessonDAO.setDisciplineId(createdLesson.getDiscipline().getDisciplineId());
                createdLessonDAO.setDate(createdLesson.getDate());
                createdLessonDAO.setClassId(createdLesson.getClasse().getClassId());
                createdLessonDAO.setLessonNumber(createdLesson.getLessonNumber());

                ModuleDAO moduleDAO = new ModuleDAO();

                moduleDAO.populate(currentModule);

                createdLessonDAO.setModule(moduleDAO);

                return createdLessonDAO;
            }
        }

        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Wrong teacher");
    }

    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @GetMapping("class/{classId}/discipline/{disciplineId}/lesson")
    @CrossOrigin
    public List<LessonDAO> getClassLessons(@PathVariable("classId") long classId, @PathVariable("disciplineId") long disciplineId, Principal principal) {

        Optional<Class> foundClass = classesRepository.findById(classId);

        if(foundClass.isPresent()) {

            Class aClass = foundClass.get();

            Boolean isAllowed = false;

            Teacher teacher =  teachersRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

            for(int i = 0; i < aClass.getTeachers().size(); i++) {
                if(aClass.getTeachers().get(i).getTeacherId().equals(teacher.getTeacherId())) {
                    isAllowed = true;
                    break;
                }
            }

            if(isAllowed) {

                Optional<Discipline> foundDiscipline = disciplinesRepository.findById(disciplineId);

                if(foundDiscipline.isPresent()) {

                    Optional<List<Lesson>> foundLessons = lessonsRepository.findAllByClasseAndDiscipline(aClass, new Discipline(disciplineId));

                    if(foundLessons.isPresent()) {

                        List<LessonDAO> lessonDAOList = populateLessonsDAOList(foundLessons.get(), classId, disciplineId);

                        return lessonDAOList;
                    }

                } else {
                    List<LessonDAO> lessonDAOS = new ArrayList<>();

                    return lessonDAOS;
                }
            }

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Class not found");
    }

    /***
     * Helper to convert from List<Lesson> to List<LessonDAO>
     *
     * @param lessons
     * @param classId
     * @param disciplineId
     * @return
     */
    private List<LessonDAO> populateLessonsDAOList(List<Lesson> lessons, long classId, long disciplineId) {

        List<LessonDAO> lessonDAOList = new ArrayList<LessonDAO>();

        for(int i = 0; i < lessons.size(); i++) {

            Lesson tempLesson = lessons.get(i);

            LessonDAO lessonDAO = new LessonDAO();

            lessonDAO.setClassId(classId);
            lessonDAO.setDate(tempLesson.getDate());
            lessonDAO.setDisciplineId(disciplineId);
            lessonDAO.setLessonId(tempLesson.getLessonId());
            lessonDAO.setLessonNumber(tempLesson.getLessonNumber());
            lessonDAO.setSummary(tempLesson.getSummary());

            lessonDAOList.add(lessonDAO);
        }

        return lessonDAOList;
    }
}
