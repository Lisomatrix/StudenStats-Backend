package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pt.lisomatrix.Sockets.models.*;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.repositories.*;
import pt.lisomatrix.Sockets.requests.models.MarkAbsence;
import pt.lisomatrix.Sockets.response.models.AbsenceResponse;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.time.LocalTime;
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

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/absence", produces= MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamSseMvc() {
        SseEmitter emitter = new SseEmitter();
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                for (int i = 0; true; i++) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data("SSE MVC - " + LocalTime.now().toString())
                            .id(String.valueOf(i))
                            .name("sse event - mvc");
                    emitter.send(event);
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
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
    public AbsenceResponse markAbsence(@RequestBody MarkAbsence markAbsence, Principal principal) throws Exception {

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

                AbsenceResponse newAbsenceResponse = new AbsenceResponse();
                newAbsenceResponse.populate(absence);

                return newAbsenceResponse;

            } else {

                absence.setJustifiable(true);
                absence.setDate(new Date());
                absence.setModule(lesson.getModule());

                Absence newAbsence = absencesRepository.save(absence);

                AbsenceResponse newAbsenceResponse = new AbsenceResponse();

                newAbsenceResponse.populate(newAbsence);

                return newAbsenceResponse;
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

                    AbsenceResponse newAbsenceResponse = new AbsenceResponse();
                    newAbsenceResponse.populate(savedAbsence);

                    deferredResult.setResult(ResponseEntity.ok().body(newAbsenceResponse));
                    return ResponseEntity.ok().body(newAbsenceResponse);
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

                    AbsenceResponse newAbsenceResponse = new AbsenceResponse();
                    newAbsenceResponse.populate(savedAbsence.getAbsence());

                    deferredResult.setResult(ResponseEntity.ok().body(newAbsenceResponse));
                    return ResponseEntity.ok().body(newAbsenceResponse);
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

                    List<AbsenceResponse> listDAO = populateDTAList(deletedAbsences);

                    deferredResult.setResult(ResponseEntity.ok(listDAO));

                    return null;
                });

        return deferredResult;
    }

    @DeleteMapping("/absence/{absenceId}")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<AbsenceResponse> removeAbsence(@PathVariable("absenceId") long absenceId, Principal principal) throws Exception {

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

                        List<AbsenceResponse> listDAO = populateDTAList(deletedAbsences);

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
    public AbsenceResponse justifyAbsence(@PathVariable("absenceId") long absenceId, Principal principal) throws Exception {

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

                AbsenceResponse absenceResponse = new AbsenceResponse();

                absenceResponse.populate(absence);

                return absenceResponse;
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

                    AbsenceResponse absenceResponse = new AbsenceResponse();

                    absenceResponse.populate(justifiedAbsence);

                    deferredResult.setResult(ResponseEntity.ok(absenceResponse));
                    return null;
                })
        .exceptionally((ex) -> {
            deferredResult.setResult(ResponseEntity.badRequest().body(ex.getMessage()));
            return null;
        });

        return deferredResult;
    }

    @PutMapping("/absence/{absenceId}/recuperate")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public AbsenceResponse recuperateAbsence(@PathVariable long absenceId, Principal principal) {

        Optional<Absence> foundAbsence = absencesRepository.findById(absenceId);

        if(foundAbsence.isPresent()) {

            Absence absence = foundAbsence.get();

            absence.setRecuperated(!absence.isRecuperated());

            Absence savedAbsence = absencesRepository.save(absence);

            AbsenceResponse absenceResponse = new AbsenceResponse();

            absenceResponse.populate(savedAbsence);

            return absenceResponse;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Absences not found");
    }

    @GetMapping("/parent/absence")
    @PreAuthorize("hasRole('ROLE_PARENT')")
    @CrossOrigin
    public List<AbsenceResponse> getChildAbsences(Principal principal) {

        Optional<List<Absence>> foundAbsence = absencesRepository.findAllByParentUserId(Long.parseLong(principal.getName()));

        return getAbsenceDAOList(foundAbsence);
    }

    @GetMapping("/student/absence")
    @PreAuthorize("hasRole('ROLE_ALUNO')")
    @CrossOrigin
    public List<AbsenceResponse> getStudentAbsences(Principal principal) {

        Optional<List<Absence>> foundAbsence = absencesRepository.findAllByStudentUserId(Long.parseLong(principal.getName()));

        return getAbsenceDAOList(foundAbsence);
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

                        List<AbsenceResponse> absenceResponseList = populateDTAList(absences);

                        deferredResult.setResult(ResponseEntity.ok(absenceResponseList));
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
    public List<AbsenceResponse> getClassAbsences(Principal principal) throws Exception {

        Optional<Class> foundClass =  classesRepository.findFirstByTeacherUserId(Long.parseLong(principal.getName()));

        if(!foundClass.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found!");
        }

        Class aClass = foundClass.get();

        List<Student> students = aClass.getStudents();

        Long[] studentIds = new Long[students.size()];

        for(int i = 0; i < students.size(); i++) {
            studentIds[i] = students.get(i).getStudentId();
        }

        Optional<List<Absence>> foundAbsences = absencesRepository.findAllByStudentId(studentIds);

        if(foundAbsences.isPresent()) {
            List<Absence> absences = foundAbsences.get();

            List<AbsenceResponse> absenceResponses = populateDTAList(absences);

            return absenceResponses;

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

                        List<AbsenceResponse> absenceResponses = populateDTAList(absences);

                        return ResponseEntity.ok().body(absenceResponses);
                    }

                    return ResponseEntity.badRequest().build();
                });
    }

    @GetMapping("/lesson/{lessonId}/absence")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<AbsenceResponse> getLessonAbsences(@PathVariable("lessonId") long lessonId) {

        Optional<List<Absence>> foundAbsences = absencesRepository.findAllByLesson(new Lesson(lessonId));

        if(foundAbsences.isPresent()) {

            List<Absence> absences = foundAbsences.get();

            List<AbsenceResponse> absenceResponseList = populateDTAList(absences);

            return absenceResponseList;
        }

        List<AbsenceResponse> absenceResponses = new ArrayList<>();

        return absenceResponses;
    }

    @GetMapping("/lesson/{lessonId}/absence/async")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    @Async
    public DeferredResult<ResponseEntity<?>> getLessonAbsencesAsync(@PathVariable("lessonId") long lessonId) {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        CompletableFuture.supplyAsync(() -> absencesRepository.findAllByLesson(new Lesson(lessonId)))
                .thenApplyAsync((foundAbsences) -> {

                    if(foundAbsences.isPresent()) {

                        List<Absence> absences = foundAbsences.get();

                        List<AbsenceResponse> absenceResponseList = populateDTAList(absences);

                        deferredResult.setResult(ResponseEntity.ok().body(absenceResponseList));
                        return null;
                    } else {
                        deferredResult.setResult(ResponseEntity.badRequest().build());
                        return null;
                    }
                });

        return deferredResult;
    }

    private List<AbsenceResponse> getAbsenceDAOList(Optional<List<Absence>> foundAbsence) {

        if(foundAbsence.isPresent()) {

            List<Absence> absences = foundAbsence.get();

            List<AbsenceResponse> absenceResponseList = populateDTAList(absences);

            return absenceResponseList;
        }

        return new ArrayList<>();
    }

    /***
     * Helper to convert List<Absence> into List<AbsenceDAO>
     *
     * @param absences
     * @return
     */
    private List<AbsenceResponse> populateDTAList(List<Absence> absences) {

        // Create new Data Access List
        List<AbsenceResponse> absenceResponseList = new ArrayList<AbsenceResponse>();

        // populate new list
        for(int i = 0; i < absences.size(); i++) {

            AbsenceResponse newAbsenceResponse = new AbsenceResponse();

            newAbsenceResponse.populate(absences.get(i));

            absenceResponseList.add(newAbsenceResponse);
        }

        return absenceResponseList;
    }
}
