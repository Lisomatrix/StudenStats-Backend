package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import pt.lisomatrix.Sockets.models.*;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.repositories.*;
import pt.lisomatrix.Sockets.requests.models.GetLessonAbsences;
import pt.lisomatrix.Sockets.requests.models.MarkAbsence;
import pt.lisomatrix.Sockets.util.SessionHandler;
import pt.lisomatrix.Sockets.websocket.models.AbsenceDAO;
import pt.lisomatrix.Sockets.websocket.models.Event;
import pt.lisomatrix.Sockets.requests.models.JustifyAbsence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class AbsencesController {

    /***
     * List of all absence types
     */
    private List<AbsenceType> absenceTypeList;

    @Autowired
    private DisciplinaryAbsencesRepository disciplinaryAbsencesRepository;

    @Autowired
    private AbsencesRepository absencesRepository;

    @Autowired
    private AbsenceTypesRepository absenceTypesRepository;

    @Autowired
    private LessonsRepository lessonsRepository;

    @Autowired
    private SessionHandler sessionHandler;

    @MessageMapping("/class/absences/create")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Event markAbsence(MarkAbsence markAbsence, StompHeaderAccessor accessor) {

        Optional<Lesson> foundLesson = lessonsRepository.findById(markAbsence.getLessonId());

        if(!foundLesson.isPresent()) {
            return new Event("BAD_REQUEST", null);
        }

        Lesson lesson = foundLesson.get();

        Class aClass = lesson.getClasse();

        Teacher teacher = (Teacher) sessionHandler.getAttribute(accessor.getSessionId(), "teacher");

        List<Teacher> teachers = aClass.getTeachers();

        Boolean isAllowed = false;

        for(int i = 0; i < teachers.size(); i++) {
            if(teachers.get(i).getTeacherId().equals(teacher.getTeacherId())) {
                isAllowed = true;
                break;
            }
        }

        if(isAllowed) {

            Absence absence = new Absence();

            AbsenceType absenceType = null;

            if(absenceTypeList == null) {
                absenceTypeList = absenceTypesRepository.findAll();
            }

            for(int i = 0; i < absenceTypeList.size(); i++) {
                if(absenceTypeList.get(i).getAbsenceTypeId().equals(markAbsence.getAbsenceType())) {
                    absenceType = absenceTypeList.get(i);
                    break;
                }
            }

            if(absenceType == null) {
                return new Event("BAD_REQUEST", null);
            }

            absence.setAbsenceType(absenceType);

            Discipline discipline = null;

            for(int i = 0; i < aClass.getDisciplines().size(); i++) {
                if(aClass.getDisciplines().get(i).getDisciplineId().equals(markAbsence.getDisciplineId())) {
                    discipline = aClass.getDisciplines().get(i);
                    break;
                }
            }

            if(discipline == null) {
                return new Event("BAD_REQUEST", null);
            }

            absence.setDiscipline(discipline);


            Student student = null;

            for(int i = 0; i < aClass.getStudents().size(); i++) {
                if(aClass.getStudents().get(i).getStudentId().equals(markAbsence.getStudentId())) {
                    student = aClass.getStudents().get(i);
                    break;
                }
            }

            if(student == null) {
                return new Event("BAD_REQUEST", null);
            }

            absence.setStudent(student);

            if(foundLesson.isPresent()) {

                absence.setLesson(foundLesson.get());

            } else {
                return new Event("BAD_REQUEST", null);
            }

            if(markAbsence.isCreate()) {

                if(absence.getAbsenceType().getName().equals("DISCIPLINAR")) {
                    if(markAbsence.getDescription().trim().equals("")) {
                        return new Event("BAD_REQUEST", null);
                    }

                    absence.setJustifiable(false);

                    DisciplinaryAbsence disciplinaryAbsence = new DisciplinaryAbsence();

                    disciplinaryAbsence.setDescription(markAbsence.getDescription());

                    absence.setDate(new Date());

                    absence.setModule(lesson.getModule());

                    Absence newAbsence = absencesRepository.save(absence);

                    disciplinaryAbsence.setAbsence(newAbsence);

                    disciplinaryAbsencesRepository.save(disciplinaryAbsence);

                    AbsenceDAO newAbsenceDAO = new AbsenceDAO();
                    newAbsenceDAO.populate(absence);

                    return new Event("ABSENCE_MARK", newAbsenceDAO);

                } else {

                    absence.setJustifiable(true);
                    absence.setDate(new Date());
                    absence.setModule(lesson.getModule());

                    Absence newAbsence = absencesRepository.save(absence);

                    AbsenceDAO newAbsenceDAO = new AbsenceDAO();

                    newAbsenceDAO.populate(newAbsence);

                    return new Event("ABSENCE_MARK", newAbsenceDAO);
                }
            } else  {
                if(absence.getAbsenceType().getName().equals("DISCIPLINAR") && !absence.getLesson().getDate().equals(new Date())) {
                    return new Event("BAD_REQUEST", null);
                }

                Optional<List<Absence>> foundDeletedAbsences = absencesRepository.deleteAbsenceByStudentAndDisciplineAndAbsenceType(absence.getStudent(),
                        absence.getDiscipline(), absence.getAbsenceType());

                if(foundDeletedAbsences.isPresent()) {

                    List<Absence> deletedAbsences = foundDeletedAbsences.get();

                    List<AbsenceDAO> listDAO = populateDTAList(deletedAbsences);

                    return new Event("ABSENCE_REMOVE", listDAO);
                }

                return new Event("BAD_REQUEST", null);
            }
        }

        return new Event("UNAUTHORIZED_ABSENCE_MARK", null);
    }

    @MessageMapping("/class/absences/justify")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Event justifyAbsence(JustifyAbsence justifyAbsence, StompHeaderAccessor accessor) {

        Class aClass = (Class) sessionHandler.getAttribute(accessor.getSessionId(), "class");

        Optional<Absence> foundAbsence = absencesRepository.findById(justifyAbsence.getAbsenceId());

        if(foundAbsence.isPresent()) {
            Absence absence = foundAbsence.get();

            List<Student> students = aClass.getStudents();

            Boolean isAllowed = false;

            for(int i = 0; i < students.size(); i++) {
                if(students.get(i).getStudentId().equals(absence.getStudent().getStudentId())) {
                    isAllowed = true;
                    break;
                }
            }

            if(isAllowed) {
                absence.setJustified(justifyAbsence.isJustified());

                absencesRepository.save(absence);

                AbsenceDAO absenceDAO = new AbsenceDAO();

                absenceDAO.populate(absence);

                return new Event("ABSENCE_JUSTIFY", absenceDAO);
            }

            return new Event("UNAUTHORIZED_ABSENCE_JUSTIFY", null);
        }

        return new Event("NOT_FOUND_ABSENCE_JUSTIFY", null);
    }

    @MessageMapping("/student/absences")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_ALUNO')")
    public Event getStudentAbsences(StompHeaderAccessor accessor) throws Exception {

        Student student = (Student) sessionHandler.getAttribute(accessor.getSessionId(), "student");

        Optional<List<Absence>> foundAbsenceListener = absencesRepository.findAllByStudent(student);

        if(foundAbsenceListener.isPresent()) {

            List<Absence> absences = foundAbsenceListener.get();

            List<AbsenceDAO> absenceDAOList = populateDTAList(absences);

            return new Event("STUDENT_ABSENCES", absenceDAOList);
        }

        return new Event("STUDENT_ABSENCES", null);
    }

    @MessageMapping("/class/absences")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Event getClassAbsences(StompHeaderAccessor accessor) throws Exception {

        Class aClass = (Class) sessionHandler.getAttribute(accessor.getSessionId(), "class");

        List<Student> students = aClass.getStudents();

        Long[] studentIds = new Long[students.size()];

        for(int i = 0; i < students.size(); i++) {
            studentIds[i] = students.get(i).getStudentId();
        }

        Optional<List<Absence>> foundAbsences = absencesRepository.findAllByStudentId(studentIds);

        if(foundAbsences.isPresent()) {
            List<Absence> absences = foundAbsences.get();

            List<AbsenceDAO> absenceDAOS = populateDTAList(absences);

            return new Event("CLASS_ABSENCES", absenceDAOS);

        } else {
            return new Event("BAD_REQUEST", null);
        }
    }

    @MessageMapping("/lesson/absences")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Event getLessonAbsences(GetLessonAbsences lesson, StompHeaderAccessor accessor) throws Exception {
    // TODO HERE IS A SECURITY PROBLEM ANY TEACHER CAN GET ALL ABSENCES FROM ALL THE CLASSES IF HE GETS THEM BY LESSON EVEN THOUGH HE DOES NOT TEACH THAT CLASS
        Optional<List<Absence>> foundAbsences = absencesRepository.findAllByLesson(new Lesson(lesson.getLessonId()));

        if(foundAbsences.isPresent()) {

            List<Absence> absences = foundAbsences.get();

            List<AbsenceDAO> absenceDAOList = populateDTAList(absences);

            return new Event("LESSON_ABSENCES", absenceDAOList);
        }

        return new Event("LESSON_ABSENCES", null);
    }

    @MessageMapping("/absences/types")
    @SendToUser("/queue/reply")
    public Event getAbsencesTypes() {

        if(absenceTypeList == null) {
            absenceTypeList = absenceTypesRepository.findAll();
        }

        return new Event("ABSENCE_TYPES", absenceTypeList);
    }

    /***
     * Helper to convert List<Absence> into List<AbsenceDAO>
     *
     * @param absences
     * @return
     */
    private List<AbsenceDAO> populateDTAList(List<Absence> absences) {

        // Create new Data Access List
        List<AbsenceDAO> absenceDAOList = new ArrayList<AbsenceDAO>();

        // populate new list
        for(int i = 0; i < absences.size(); i++) {

            AbsenceDAO newAbsenceDAO = new AbsenceDAO();

            newAbsenceDAO.populate(absences.get(i));

            absenceDAOList.add(newAbsenceDAO);
        }

        return absenceDAOList;
    }
}
