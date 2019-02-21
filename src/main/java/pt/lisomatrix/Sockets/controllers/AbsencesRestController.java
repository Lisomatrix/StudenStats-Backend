package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.*;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.repositories.*;
import pt.lisomatrix.Sockets.requests.models.MarkAbsence;
import pt.lisomatrix.Sockets.websocket.models.AbsenceDAO;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@RestController
public class AbsencesRestController {

    private List<AbsenceType> absenceTypeList;

    @Autowired
    private TeachersRepository teachersRepository;

    @Autowired
    private LessonsRepository lessonsRepository;

    @Autowired
    private AbsenceTypesRepository absenceTypesRepository;

    @Autowired
    private AbsencesRepository absencesRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private DisciplinaryAbsencesRepository disciplinaryAbsencesRepository;

    @PostConstruct
    public void setAbsenceTypes() {
        absenceTypeList = absenceTypesRepository.findAll();
    }

    @GetMapping("/absence/types")
    @CrossOrigin
    public List<AbsenceType> getAbsenceType() {

        if(absenceTypeList == null) {
            absenceTypeList = absenceTypesRepository.findAll();
        }

        return absenceTypeList;
    }

    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @PostMapping("/absence")
    @CrossOrigin
    public AbsenceDAO markAbsence(@RequestBody MarkAbsence markAbsence, Principal principal) throws Exception {

        Optional<Lesson> foundLesson = lessonsRepository.findById(markAbsence.getLessonId());

        if(!foundLesson.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Lesson not found");
        }

        Lesson lesson = foundLesson.get();
        Class aClass = lesson.getClasse();

        Teacher teacher = teachersRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

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
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid absence type");
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
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid discipline");
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
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid student");
            }

            absence.setStudent(student);

            if(foundLesson.isPresent()) {

                absence.setLesson(foundLesson.get());

            } else {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Lesson not found");
            }

            if(absence.getAbsenceType().getName().equals("DISCIPLINAR")) {
                if(markAbsence.getDescription() == null || markAbsence.getDescription().trim().equals("")) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "A description is needed");
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

                //throw new Exception();
                return newAbsenceDAO;

            } else {

                absence.setJustifiable(true);
                absence.setDate(new Date());
                absence.setModule(lesson.getModule());

                Absence newAbsence = absencesRepository.save(absence);

                AbsenceDAO newAbsenceDAO = new AbsenceDAO();

                newAbsenceDAO.populate(newAbsence);

                return newAbsenceDAO;
            }

        }

        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @PostMapping("/absence/async")
    @CrossOrigin
    public DeferredResult<ResponseEntity<?>> markAbsenceAsync(@RequestBody MarkAbsence markAbsence, Principal principal) {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult();
        CompletableFuture<Optional<Lesson>> futureLesson = lessonsRepository.findByLessonId(markAbsence.getAbsenceId());
        CompletableFuture<Teacher> futureTeacher = teachersRepository.findFirstByUserIdAsync(Long.parseLong(principal.getName()));

        CompletableFuture.allOf(futureLesson, futureTeacher)
                .thenApply((x) -> {

                    Optional<Lesson> foundLesson = futureLesson.join();
                    Teacher teacher = futureTeacher.join();

                    if(!foundLesson.isPresent()) {
                        deferredResult.setResult(ResponseEntity.badRequest().build());
                        return new Absence();
                    }

                    Lesson lesson = foundLesson.get();
                    Class aClass = lesson.getClasse();

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


                        for(int i = 0; i < absenceTypeList.size(); i++) {
                            if(absenceTypeList.get(i).getAbsenceTypeId().equals(markAbsence.getAbsenceType())) {
                                absenceType = absenceTypeList.get(i);
                                break;
                            }
                        }

                        if(absenceType == null) {
                            deferredResult.setResult(ResponseEntity.badRequest().build());
                            return new Absence();
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
                            deferredResult.setResult(ResponseEntity.badRequest().build());
                            return new Absence();
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
                            deferredResult.setResult(ResponseEntity.badRequest().build());
                            return new Absence();
                        }

                        absence.setStudent(student);


                        if(foundLesson.isPresent()) {

                            absence.setLesson(foundLesson.get());

                        } else {
                            deferredResult.setResult(ResponseEntity.badRequest().build());
                            return new Absence();
                        }

                        absence.setJustifiable(true);
                        absence.setDate(new Date());
                        absence.setModule(lesson.getModule());

                        deferredResult.setResult(ResponseEntity.badRequest().build());
                        return absence;
                    } else {
                        deferredResult.setResult(ResponseEntity.badRequest().build());
                        return new Absence();
                    }
                }).thenApplyAsync((y) -> absencesRepository.save(y))
                .thenApply((savedAbsence) -> {

                    AbsenceDAO newAbsenceDAO = new AbsenceDAO();
                    newAbsenceDAO.populate(savedAbsence);

                    deferredResult.setResult(ResponseEntity.ok().body(newAbsenceDAO));
                    return ResponseEntity.ok().body(newAbsenceDAO);
                });

        return deferredResult;
    }

    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @PostMapping("/absence/disciplinary/async")
    @CrossOrigin
    public DeferredResult<ResponseEntity<?>> markDisciplinaryAbsenceAsync(@RequestBody MarkAbsence markAbsence, Principal principal) {

        DeferredResult deferredResult = new DeferredResult();
        CompletableFuture<Optional<Lesson>> futureLesson = lessonsRepository.findByLessonId(markAbsence.getAbsenceId());
        CompletableFuture<Teacher> futureTeacher = teachersRepository.findFirstByUserIdAsync(Long.parseLong(principal.getName()));

        CompletableFuture.allOf(futureLesson, futureTeacher)
                .thenApply((x) -> {

                    Optional<Lesson> foundLesson = futureLesson.join();
                    Teacher teacher = futureTeacher.join();

                    if(!foundLesson.isPresent()) {
                        deferredResult.setResult(ResponseEntity.badRequest().build());
                        return new DisciplinaryAbsence();
                    }

                    Lesson lesson = foundLesson.get();
                    Class aClass = lesson.getClasse();

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


                        for(int i = 0; i < absenceTypeList.size(); i++) {
                            if(absenceTypeList.get(i).getAbsenceTypeId().equals(markAbsence.getAbsenceType())) {
                                absenceType = absenceTypeList.get(i);
                                break;
                            }
                        }

                        if(absenceType == null) {
                            deferredResult.setResult(ResponseEntity.badRequest().build());
                            return new DisciplinaryAbsence();
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
                            deferredResult.setResult(ResponseEntity.badRequest().build());
                            return new DisciplinaryAbsence();
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
                            deferredResult.setResult(ResponseEntity.badRequest().build());
                            return new DisciplinaryAbsence();
                        }

                        absence.setStudent(student);


                        if(foundLesson.isPresent()) {

                            absence.setLesson(foundLesson.get());

                        } else {
                            deferredResult.setResult(ResponseEntity.badRequest().build());
                            return new DisciplinaryAbsence();
                        }

                        absence.setJustifiable(false);

                        DisciplinaryAbsence disciplinaryAbsence = new DisciplinaryAbsence();

                        disciplinaryAbsence.setDescription(markAbsence.getDescription());

                        absence.setDate(new Date());

                        absence.setModule(lesson.getModule());

                        deferredResult.setResult(ResponseEntity.badRequest().build());
                        return disciplinaryAbsence;
                    } else {
                        deferredResult.setResult(ResponseEntity.badRequest().build());
                        return new DisciplinaryAbsence();
                    }
                }).thenApplyAsync((y) -> disciplinaryAbsencesRepository.save(y))
                .thenApply((savedAbsence) -> {

                    AbsenceDAO newAbsenceDAO = new AbsenceDAO();
                    newAbsenceDAO.populate(savedAbsence.getAbsence());

                    deferredResult.setResult(ResponseEntity.ok().body(newAbsenceDAO));
                    return ResponseEntity.ok().body(newAbsenceDAO);
                });

        return deferredResult;
    }

    @DeleteMapping("/absence/{absenceId}/async")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public DeferredResult<ResponseEntity<?>> removeAbsenceAsync(@PathVariable("absenceId") long absenceId, Principal principal) throws Exception {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        CompletableFuture<Teacher> futureTeacher = teachersRepository.findFirstByUserIdAsync(Long.parseLong(principal.getName()));
        CompletableFuture<Optional<Absence>> futureAbsence = CompletableFuture.supplyAsync(() -> absencesRepository.findById(absenceId));

        CompletableFuture.allOf(futureTeacher, futureAbsence)
                .thenApplyAsync((x) -> {

                    Optional<Absence> foundAbsence = futureAbsence.join();
                    Teacher teacher = futureTeacher.join();

                    if(foundAbsence.isPresent()) {
                        Absence absence = foundAbsence.get();
                        Class aClass = absence.getLesson().getClasse();

                        if(absence.getAbsenceType().getName().equals("DISCIPLINAR")) {
                            deferredResult.setResult(ResponseEntity.badRequest().build());
                            throw new ResponseStatusException(
                                    HttpStatus.UNAUTHORIZED, "Unauthorized");
                        }

                        List<Teacher> teachers = aClass.getTeachers();

                        Boolean isAllowed = false;

                        for(int i = 0; i < teachers.size(); i++) {
                            if(teachers.get(i).getTeacherId().equals(teacher.getTeacherId())) {
                                isAllowed = true;
                                break;
                            }
                        }

                        if(isAllowed) {
                            return absence;

                        } else {
                            deferredResult.setResult(ResponseEntity.badRequest().build());
                            throw new ResponseStatusException(
                                    HttpStatus.UNAUTHORIZED, "Unauthorized");
                        }

                    } else {
                        deferredResult.setResult(ResponseEntity.badRequest().build());
                        throw new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "Unauthorized");
                    }
                }).thenApplyAsync((absence) -> absencesRepository.deleteAbsenceByStudentAndDisciplineAndAbsenceType(absence.getStudent(),
                absence.getDiscipline(), absence.getAbsenceType()))
                .thenApplyAsync((foundDeletedAbsences) -> {
                    List<Absence> deletedAbsences = foundDeletedAbsences.get();

                    List<AbsenceDAO> listDAO = populateDTAList(deletedAbsences);

                    deferredResult.setResult(ResponseEntity.ok(listDAO));

                    return null;
                });

        return deferredResult;
    }

    @DeleteMapping("/absence/{absenceId}")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<AbsenceDAO> removeAbsence(@PathVariable("absenceId") long absenceId, Principal principal) throws Exception {

        Optional<Absence> foundAbsence = absencesRepository.findById(absenceId);

        if(foundAbsence.isPresent()) {

            Absence absence = foundAbsence.get();
            Class aClass = absence.getLesson().getClasse();

            Teacher teacher = teachersRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

            if(!absence.getAbsenceType().getName().equals("DISCIPLINAR")) {

                List<Teacher> teachers = aClass.getTeachers();

                Boolean isAllowed = false;

                for(int i = 0; i < teachers.size(); i++) {
                    if(teachers.get(i).getTeacherId().equals(teacher.getTeacherId())) {
                        isAllowed = true;
                        break;
                    }
                }

                if(isAllowed) {

                    Optional<List<Absence>> foundDeletedAbsences = absencesRepository.deleteAbsenceByStudentAndDisciplineAndAbsenceType(absence.getStudent(),
                            absence.getDiscipline(), absence.getAbsenceType());

                    if(foundDeletedAbsences.isPresent()) {

                        List<Absence> deletedAbsences = foundDeletedAbsences.get();

                        List<AbsenceDAO> listDAO = populateDTAList(deletedAbsences);

                        return listDAO;
                    }
                } else {
                    throw new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED, "Unauthorized");
                }
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Absence not found");
    }

    @PutMapping("/absence/{absenceId}")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public AbsenceDAO justifyAbsence(@PathVariable("absenceId") long absenceId, Principal principal) throws Exception {

        Optional<Absence> foundAbsence = absencesRepository.findById(absenceId);

        if(foundAbsence.isPresent()) {

            Absence absence = foundAbsence.get();

            Class aClass = classesRepository.findFirstByTeacherUserId(Long.parseLong(principal.getName())).get();

            List<Student> students = aClass.getStudents();

            Boolean isAllowed = false;

            for(int i = 0; i < students.size(); i++) {
                if(students.get(i).getStudentId().equals(absence.getStudent().getStudentId())) {
                    isAllowed = true;
                    break;
                }
            }

            if(isAllowed) {
                absence.setJustified(true);

                absencesRepository.save(absence);

                AbsenceDAO absenceDAO = new AbsenceDAO();

                absenceDAO.populate(absence);

                return absenceDAO;
            } else {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Absence not found");
    }

    @PutMapping("/absence/{absenceId}/async")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public DeferredResult<ResponseEntity<?>> justifyAbsenceAsync(@PathVariable("absenceId") long absenceId, Principal principal) throws Exception {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
        CompletableFuture<Optional<Absence>> futureAbsence = CompletableFuture.supplyAsync(() -> absencesRepository.findById(absenceId));
        CompletableFuture<Optional<Class>> futureClass = CompletableFuture.supplyAsync(() -> classesRepository.findFirstByClassDirectorUserId(Long.parseLong(principal.getName())));

        CompletableFuture.allOf(futureAbsence, futureClass)
                .thenApplyAsync((x) -> {

                    Optional<Absence> foundAbsence = futureAbsence.join();
                    Optional<Class> foundClass = futureClass.join();

                    if(foundAbsence.isPresent() && foundClass.isPresent()) {

                        Absence absence = foundAbsence.get();
                        Class aClass = foundClass.get();

                        List<Student> students = aClass.getStudents();

                        Boolean isAllowed = false;

                        for(int i = 0; i < students.size(); i++) {
                            if(students.get(i).getStudentId().equals(absence.getStudent().getStudentId())) {
                                isAllowed = true;
                                break;
                            }
                        }

                        if(isAllowed) {
                            absence.setJustified(true);

                            return absence;

                        } else {
                            deferredResult.setResult(ResponseEntity.badRequest().build());
                            throw new ResponseStatusException(
                                    HttpStatus.UNAUTHORIZED, "Unauthorized");
                        }

                    } else {
                        deferredResult.setResult(ResponseEntity.badRequest().build());
                        throw new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "Unauthorized");
                    }

                })
                .thenApplyAsync((absence) -> absencesRepository.save(absence))
                .thenApplyAsync((justifiedAbsence) -> {

                    AbsenceDAO absenceDAO = new AbsenceDAO();

                    absenceDAO.populate(justifiedAbsence);

                    deferredResult.setResult(ResponseEntity.ok(absenceDAO));
                    return null;
                })
        .exceptionally((ex) -> {
            deferredResult.setResult(ResponseEntity.badRequest().body(ex.getMessage()));
            return null;
        });

        return deferredResult;
    }

    @GetMapping("/student/absence")
    @PreAuthorize("hasRole('ROLE_ALUNO')")
    @CrossOrigin
    public List<AbsenceDAO> getStudentAbsences(Principal principal) {

        Optional<List<Absence>> foundAbsenceListener = absencesRepository.findAllByStudentUserId(Long.parseLong(principal.getName()));

        if(foundAbsenceListener.isPresent()) {

            List<Absence> absences = foundAbsenceListener.get();

            List<AbsenceDAO> absenceDAOList = populateDTAList(absences);

            return absenceDAOList;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Absences not found");
    }

    @GetMapping("/student/absence/async")
    @PreAuthorize("hasRole('ROLE_ALUNO')")
    @CrossOrigin
    public DeferredResult<ResponseEntity<?>> getStudentAbsencesAsync(Principal principal) {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        CompletableFuture.supplyAsync(() -> absencesRepository.findAllByStudentUserId(Long.parseLong(principal.getName())))
                .thenApplyAsync((foundAbsences) -> {

                    if(foundAbsences.isPresent()) {

                        List<Absence> absences = foundAbsences.get();

                        List<AbsenceDAO> absenceDAOList = populateDTAList(absences);

                        deferredResult.setResult(ResponseEntity.ok(absenceDAOList));
                        return null;
                    } else {
                        deferredResult.setResult(ResponseEntity.badRequest().build());
                        return null;
                    }
                });

        return deferredResult;
    }

    @GetMapping("/class/absence")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<AbsenceDAO> getClassAbsences(Principal principal) throws Exception {

        Class aClass = classesRepository.findFirstByTeacherUserId(Long.parseLong(principal.getName())).get();
        /*try {
            Thread.sleep(2000);
        }catch (Exception ex) {

        }*/
        List<Student> students = aClass.getStudents();

        Long[] studentIds = new Long[students.size()];

        for(int i = 0; i < students.size(); i++) {
            studentIds[i] = students.get(i).getStudentId();
        }

        Optional<List<Absence>> foundAbsences = absencesRepository.findAllByStudentId(studentIds);

        if(foundAbsences.isPresent()) {
            List<Absence> absences = foundAbsences.get();

            List<AbsenceDAO> absenceDAOS = populateDTAList(absences);

            return absenceDAOS;

        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No absences found");
        }
    }

    @GetMapping("/class/absence/async")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    @Async(value = "taskExecutor")
    public CompletableFuture<ResponseEntity<?>> getClassAbsencesAsync(Principal principal) throws Exception {

        return CompletableFuture.supplyAsync(() -> classesRepository.findFirstByTeacherUserId(Long.parseLong(principal.getName())))
                .thenApplyAsync((foundClass) -> {

                    /*try {
                        Thread.sleep(2000);
                    }catch (Exception ex) {

                    }*/

                    if(foundClass.isPresent()) {

                        Class aClass = foundClass.get();

                        List<Student> students = aClass.getStudents();

                        Long[] studentIds = new Long[students.size()];

                        for(int i = 0; i < students.size(); i++) {
                            studentIds[i] = students.get(i).getStudentId();
                        }

                        return studentIds;
                    }

                    return new Long[0];
                }).thenApplyAsync(s -> absencesRepository.findAllByStudentId(s))
                .thenApply((foundAbsences) -> {
                    if(foundAbsences.isPresent()) {
                        List<Absence> absences = foundAbsences.get();

                        List<AbsenceDAO> absenceDAOS = populateDTAList(absences);

                        return ResponseEntity.ok().body(absenceDAOS);
                    }

                    return ResponseEntity.badRequest().build();
                });
    }

    @GetMapping("/lesson/{lessonId}/absence")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<AbsenceDAO> getLessonAbsences(@PathVariable("lessonId") long lessonId) {

        Optional<List<Absence>> foundAbsences = absencesRepository.findAllByLesson(new Lesson(lessonId));

        if(foundAbsences.isPresent()) {

            List<Absence> absences = foundAbsences.get();

            List<AbsenceDAO> absenceDAOList = populateDTAList(absences);

            return absenceDAOList;
        }

        List<AbsenceDAO> absenceDAOS = new ArrayList<>();

        return absenceDAOS;
    }

    @GetMapping("/lesson/{lessonId}/absence/async")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    @Async
    public DeferredResult<ResponseEntity<?>> getLessonAbsencesAsync(@PathVariable("lessonId") long lessonId) {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        CompletableFuture.supplyAsync(() -> absencesRepository.findAllByLesson(new Lesson(lessonId)))
                .thenApplyAsync((foundAbsences) -> {
                    /*try {
                        Thread.sleep(2000);
                    }catch (Exception ex) {

                    }*/
                    if(foundAbsences.isPresent()) {

                        List<Absence> absences = foundAbsences.get();

                        List<AbsenceDAO> absenceDAOList = populateDTAList(absences);

                        deferredResult.setResult(ResponseEntity.ok().body(absenceDAOList));
                        return null;
                    } else {
                        deferredResult.setResult(ResponseEntity.badRequest().build());
                        return null;
                    }
                });

        return deferredResult;
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
