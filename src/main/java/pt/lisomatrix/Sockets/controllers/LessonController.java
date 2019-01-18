package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.*;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Module;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.DisciplinesRepository;
import pt.lisomatrix.Sockets.repositories.LessonsRepository;
import pt.lisomatrix.Sockets.repositories.ModulesRepository;
import pt.lisomatrix.Sockets.requests.models.GetClassLessons;
import pt.lisomatrix.Sockets.requests.models.NewLesson;
import pt.lisomatrix.Sockets.requests.models.UpdateLessonSummary;
import pt.lisomatrix.Sockets.util.SessionHandler;
import pt.lisomatrix.Sockets.websocket.models.Event;
import pt.lisomatrix.Sockets.websocket.models.LessonDAO;
import pt.lisomatrix.Sockets.websocket.models.ModuleDAO;

import java.util.*;


@Controller
public class LessonController {

    @Autowired
    private LessonsRepository lessonsRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private ModulesRepository modulesRepository;

    @Autowired
    private DisciplinesRepository disciplinesRepository;

    @Autowired
    private SessionHandler sessionHandler;

    @MessageMapping("/lessons/summary")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Event updateLessonSummary(UpdateLessonSummary updateLessonSummary, StompHeaderAccessor accessor) {

        Optional<Lesson> foundLesson = lessonsRepository.findById(updateLessonSummary.getLessonId());

        if(foundLesson.isPresent()) {

            Lesson lesson = foundLesson.get();

            Teacher teacher = (Teacher) sessionHandler.getAttribute(accessor.getSessionId(), "teacher");

            if(lesson.getTeacher().getTeacherId().equals(teacher.getTeacherId())) {

                lessonsRepository.updateSummary(updateLessonSummary.getSummary(), lesson.getLessonId());

                return new Event("SUMMARY_UPDATED", null);
            }
        }

        return new Event("BAD_REQUEST", null);
    }

    @MessageMapping("/lessons/create")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Event newLesson(NewLesson newLesson, StompHeaderAccessor accessor) {

        Teacher teacher = (Teacher) sessionHandler.getAttribute(accessor.getSessionId(), "teacher");

        if(newLesson.getTeacherId() == teacher.getTeacherId()) {

            Date currentDate = new Date();

            Lesson lesson = new Lesson();

            Optional<Discipline> foundDiscipline = disciplinesRepository.findById(newLesson.getDisciplineId());

            if(!foundDiscipline.isPresent()) {
                return new Event("BAD_REQUEST", null);
            }

            lesson.setClasse(new Class(newLesson.getClassId()));
            lesson.setDiscipline(foundDiscipline.get());
            lesson.setTeacher(teacher);
            lesson.setDate(currentDate);

            Optional<List<Module>> foundModules = modulesRepository.findAllByDiscipline(foundDiscipline.get());

            if(!foundModules.isPresent()) {
                return new Event("BAD_REQUEST", null);
            }

            List<Module> modules = foundModules.get();

            Optional<Lesson> foundLesson = lessonsRepository.findFirstByClasseAndDisciplineOrderByLessonNumberDesc(new Class(newLesson.getClassId()), new Discipline(newLesson.getDisciplineId()));

            if(foundLesson.isPresent()) {

                Lesson lastLesson = foundLesson.get();

                final int lastLessonNumber = lastLesson.getLessonNumber() + 1;

                lesson.setLessonNumber(lastLessonNumber);

                Module currentModule = null;

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
                    return new Event("BAD_REQUEST", null);
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

                return new Event("NEW_LESSON", createdLessonDAO);

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
                    return new Event("BAD_REQUEST", null);
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

                return new Event("NEW_LESSON", createdLessonDAO);
            }
        }

        return new Event("BAD_REQUEST", null);
    }

    @MessageMapping("/class/lessons")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasRole('ROLE_ALUNO')")
    public Event getClassLessons(GetClassLessons getClassLessons, StompHeaderAccessor accessor) {

        Optional<Class> foundClass = classesRepository.findById(getClassLessons.getClassId());

        if(foundClass.isPresent()) {

            Class aClass = foundClass.get();

            String role = (String) sessionHandler.getAttribute(accessor.getSessionId(), "role");

            Boolean isAllowed = false;

            if(role.equals(Roles.PROFESSOR.toString())) {

                Teacher teacher = (Teacher) sessionHandler.getAttribute(accessor.getSessionId(), "teacher");

                for(int i = 0; i < aClass.getTeachers().size(); i++) {
                    if(aClass.getTeachers().get(i).getTeacherId().equals(teacher.getTeacherId())) {
                        isAllowed = true;
                        break;
                    }
                }
            } else if(role.equals(Roles.ALUNO.toString())) {

                Student student = (Student) sessionHandler.getAttribute(accessor.getSessionId(), "student");

                for(int i = 0; i < aClass.getStudents().size(); i++) {
                    if(aClass.getStudents().get(i).getStudentId().equals(student.getStudentId())) {
                        isAllowed = true;
                        break;
                    }
                }
            }

            if(isAllowed) {

                Optional<List<Lesson>> foundLessons = lessonsRepository.findAllByClasseAndDiscipline(aClass, new Discipline(getClassLessons.getDisciplineId()));

                if(foundLessons.isPresent()) {

                    List<LessonDAO> lessonDAOList = populateLessonsDAOList(foundLessons.get(), getClassLessons.getClassId(), getClassLessons.getDisciplineId());

                    return new Event("GET_LESSONS", lessonDAOList);
                }
            }
        }

        return new Event("GET_LESSONS", Collections.EMPTY_LIST);
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
