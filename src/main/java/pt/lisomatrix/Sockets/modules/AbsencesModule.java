package pt.lisomatrix.Sockets.modules;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.*;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.repositories.*;
import pt.lisomatrix.Sockets.requests.models.GetClassLessons;
import pt.lisomatrix.Sockets.requests.models.GetLessonAbsences;
import pt.lisomatrix.Sockets.websocket.models.AbsenceDAO;
import pt.lisomatrix.Sockets.websocket.models.JustifyAbsence;
import pt.lisomatrix.Sockets.requests.models.MarkAbsence;

import java.util.*;

/***
 * Listens and handles all WebSocket messages related to absences
 *
 */
@Component
public class AbsencesModule {

    /***
     * List of all absence types
     */
    private List<AbsenceType> absenceTypeList;

    /***
     * WebSockets server object
     */
    private SocketIOServer server;

    /***
     * Absences Repository to save and update
     */
    private AbsencesRepository absencesRepository;

    /***
     * Disciplinary Absences Repository to save and update
     */
    private DisciplinaryAbsencesRepository disciplinaryAbsencesRepository;

    /***
     * REDIS Users Storage repository to get in ram info
     */
    private RedisUsersStorageRepository redisUsersStorageRepository;

    /***
     * Absence types Repository to get info
     */
    private AbsenceTypesRepository absenceTypesRepository;

    /***
     * Disciplines Repository to get info
     */
    private DisciplinesRepository disciplinesRepository;

    /***
     * Students Repository to get info
     */
    private StudentsRepository studentsRepository;

    /***
     * Lessons Repository to get info
     */
    private LessonsRepository lessonsRepository;

    /***
     * Initialize all class dependencies and
     * Start listening to specific WebSocket messages
     *
     * @param server
     * @param absencesRepository
     * @param redisUsersStorageRepository
     * @param absenceTypesRepository
     * @param disciplinesRepository
     * @param studentsRepository
     * @param lessonsRepository
     * @param disciplinaryAbsencesRepository
     */
    @Autowired
    public AbsencesModule(SocketIOServer server, AbsencesRepository absencesRepository, RedisUsersStorageRepository
            redisUsersStorageRepository, AbsenceTypesRepository absenceTypesRepository,
                          DisciplinesRepository disciplinesRepository, StudentsRepository studentsRepository,
                          LessonsRepository lessonsRepository, DisciplinaryAbsencesRepository disciplinaryAbsencesRepository) {

        this.server = server;
        this.absencesRepository = absencesRepository;
        this.redisUsersStorageRepository = redisUsersStorageRepository;
        this.absenceTypesRepository = absenceTypesRepository;
        this.disciplinesRepository = disciplinesRepository;
        this.studentsRepository = studentsRepository;
        this.lessonsRepository = lessonsRepository;
        this.disciplinaryAbsencesRepository = disciplinaryAbsencesRepository;

        // Listen for the following events
        this.server.addEventListener("GET_ABSENCES", GetLessonAbsences.class, new DataListener<GetLessonAbsences>() {

            @Override
            public void onData(final SocketIOClient client, GetLessonAbsences data, final AckRequest ackRequest) {

                getAbsences(client, data, ackRequest, client.getSessionId().toString());
            }
        });

        this.server.addEventListener("ABSENCE_JUSTIFY", JustifyAbsence.class, new DataListener<JustifyAbsence>() {
            @Override
            public void onData(SocketIOClient client, JustifyAbsence justifyAbsence, AckRequest ackRequest) throws Exception {

                justifyAbsence(client, ackRequest, client.getSessionId().toString(), justifyAbsence);
            }
        });

        this.server.addEventListener("ABSENCE_MARK", MarkAbsence.class, new DataListener<MarkAbsence>() {
            @Override
            public void onData(SocketIOClient socketIOClient, MarkAbsence markAbsence, AckRequest ackRequest) throws Exception {

                markAbsence(socketIOClient, ackRequest, socketIOClient.getSessionId().toString(), markAbsence);
            }
        });

        this.server.addEventListener("GET_ABSENCE_TYPES", Absence.class, new DataListener<Absence>() {
            @Override
            public void onData(SocketIOClient socketIOClient, Absence absence, AckRequest ackRequest) throws Exception {
                getAbsenceTypes(socketIOClient);
            }
        });

        this.server.addEventListener("GET_CLASS_ABSENCES", GetClassLessons.class, new DataListener<GetClassLessons>() {
            @Override
            public void onData(SocketIOClient client, GetClassLessons data, AckRequest ackSender) throws Exception {
                getClassAbsences(client, ackSender, data, client.getSessionId().toString());
            }
        });
    }

    /***
     * Handle absence marking logic
     *
     * @param client
     * @param request
     * @param sessionId
     * @param markAbsence
     */
    private void markAbsence(SocketIOClient client, AckRequest request, String sessionId, MarkAbsence markAbsence)  {

        // Get User Redis Storage
        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        // Check if found
        if(foundRedisUserStorage.isPresent()) {

            // Get it
            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            // Check is a teacher
            if(redisUserStorage.getRole().equals(Roles.PROFESSOR.toString())) {

                // Get a absence object from a mark absence object
                Absence absence = populateAbsence(markAbsence);

                // If correctly converted
                if(absence != null) {

                    absence.setRecuperated(false);

                    // Check the is supposed to delete or create
                    // If its to create absence
                    if(markAbsence.isCreate()) {

                        // Check if the absence time is disciplinary
                        if(absence.getAbsenceType().getName().equals("DISCIPLINAR")) {
                            // Check if a description was sent
                            if(markAbsence.getDescription().trim().equals("")) {
                                client.sendEvent("BAD_REQUEST");
                                return;
                            }

                            // Create disciplinary absence object and populate it
                            DisciplinaryAbsence disciplinaryAbsence = new DisciplinaryAbsence();

                            disciplinaryAbsence.setDescription(markAbsence.getDescription());

                            // Save absence
                            Absence newAbsence = absencesRepository.save(absence);

                            // End populating disciplinary absence
                            disciplinaryAbsence.setAbsence(newAbsence);

                            // Save disciplinary absence
                            disciplinaryAbsencesRepository.save(disciplinaryAbsence);

                            // Populate absenceDAO
                            AbsenceDAO newAbsenceDAO = populateDTA(absence);

                            // Send it to user
                            client.sendEvent("ABSENCE_MARK", newAbsenceDAO);

                        } else {

                            // If not disciplinary then simply insert it
                            absencesRepository.save(absence);

                            // Populate absenceDAO
                            AbsenceDAO newAbsence = populateDTA(absence);

                            // Send it to user
                            client.sendEvent("ABSENCE_MARK", newAbsence);
                        }

                    } else {
                        // If its to delete absence

                        // Check if its a disciplinary absence
                        if(absence.getAbsenceType().getName().equals("DISCIPLINAR") && !absence.getLesson().getDate().equals(new Date())) {
                            // If so then sent error
                            client.sendEvent("BAD_REQUEST");
                            return;
                        }

                        // Delete the absences and get list of deleted absences
                        Optional<List<Absence>> foundDeletedAbsences = absencesRepository.deleteAbsenceByStudentAndDisciplineAndAbsenceType(absence.getStudent(),
                                absence.getDiscipline(), absence.getAbsenceType());

                        // If any absence was deleted
                        if(foundDeletedAbsences.isPresent()) {

                            // Get them
                           List<Absence> deletedAbsences = foundDeletedAbsences.get();

                           // Populate AbsenceDAO List
                           List<AbsenceDAO> listDAO = populateDTAList(deletedAbsences);

                           // Send it to user
                           client.sendEvent("ABSENCE_REMOVE", listDAO);

                        } else {
                            client.sendEvent("BAD_REQUEST");
                        }
                    }

                } else {
                    client.sendEvent("ABSENCE_ERROR");
                }

            } else {
                client.sendEvent("UNAUTHORIZED");
            }
        } else {
            client.disconnect();
        }

    }

    /***
     * Handle absence justifying logic
     *
     * @param client
     * @param request
     * @param sessionId
     * @param justifyAbsence
     */
    private void justifyAbsence(SocketIOClient client, AckRequest request, String sessionId, JustifyAbsence justifyAbsence) {

        // Find redisUserStorage
        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        // If found
        if(foundRedisUserStorage.isPresent()) {

            // Get it
            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            // Check if is authorized
            if(redisUserStorage.getRole().equals(Roles.PROFESSOR.toString())) {

                // Find Absence
                Optional<Absence> foundAbsence = absencesRepository.findById(justifyAbsence.getAbsenceId());

                // If found
                if(foundAbsence.isPresent()) {

                    // Get it
                    Absence absence = foundAbsence.get();

                    // Set it justified
                    absence.setJustified(justifyAbsence.isJustified());

                    // Save it
                    absencesRepository.save(absence);

                    // Warn Client
                    client.sendEvent("ABSENCE_JUSTIFY");

                } else {
                    // Warn Client
                    client.sendEvent("ABSENCE_ERROR");
                }

            } else {
                // Warn Client
                client.sendEvent("UNAUTHORIZED");
            }

        } else {
            // Disconnect User
            client.disconnect();
        }

    }

    private void getClassAbsences(SocketIOClient client, AckRequest request, GetClassLessons getClassLessons, String sessionId) {

        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        if(foundRedisUserStorage.isPresent()) {

            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            Class userClass = redisUserStorage.getTeacherClass();

            List<Student> students = userClass.getStudents();
            String[] studentIds = new String[students.size()];

            for(int i = 0; i < students.size(); i++) {
                studentIds[i] = students.get(i).getStudentId();
            }

            Optional<List<Absence>> foundAbsences = absencesRepository.findAllByStudentId(studentIds);

            if(foundAbsences.isPresent()) {
                List<Absence> absences = foundAbsences.get();

                List<AbsenceDAO> absenceDAOS = populateDTAList(absences);

                client.sendEvent("CLASS_ABSENCES", absenceDAOS);

            } else {
                client.sendEvent("NOT_FOUND");
            }

        } else {
            client.disconnect();
        }
    }

    /***
     * Handle getting absences logic
     *
     * @param client
     * @param getLessonAbsences
     * @param request
     * @param sessionId
     */
    private void getAbsences(SocketIOClient client, GetLessonAbsences getLessonAbsences, AckRequest request, String sessionId)  {

        if(getLessonAbsences == null) {
            return;
        }

        // Get redisUserStorage
        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        // If found
        if(foundRedisUserStorage.isPresent()) {

            // Get it
            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            if(redisUserStorage.getRole().equals(Roles.ALUNO.toString())) {

                // Get student from storage
                Student student = redisUserStorage.getStudent();

                // Get absences
                List<Absence> absences = retrieveAbsences(student);

                if(absences != null || absences.size() > 0) {

                    // Get populated data access list
                    List<AbsenceDAO> absenceDAOList = populateDTAList(absences);

                    // Send List
                    client.sendEvent("GET_ABSENCES", absenceDAOList);
                } else {

                    // Get absences from database
                    List<Absence> foundAbsences = retrieveAbsences(student);

                    if(foundAbsences != null) {

                        // Get populated data access list
                        List<AbsenceDAO> absenceDAOList = populateDTAList(absences);

                        // Send List
                        client.sendEvent("GET_ABSENCES", absenceDAOList);
                    }
                }

            } else if(redisUserStorage.getRole().equals(Roles.PROFESSOR.toString())) {

                // Get lesson
                Optional<Lesson> foundLesson = lessonsRepository.findById(getLessonAbsences.getLessonId());

                // If found
                if(foundLesson.isPresent()) {

                    // Get it
                    Lesson lesson = foundLesson.get();

                    // Get absences
                    Optional<List<Absence>> foundAbsences = absencesRepository.findAllByLesson(lesson);

                    // If found
                    if(foundAbsences.isPresent()) {

                        // Get them
                        List<Absence> absences = foundAbsences.get();

                        // Get populated data access list
                        List<AbsenceDAO> absenceDAOList = populateDTAList(absences);

                        // Send List
                        client.sendEvent("GET_ABSENCES", absenceDAOList);

                        return;

                    } else {

                        // Create empty List
                        List<AbsenceDAO> absenceDAOList = new ArrayList<AbsenceDAO>();

                        // Send List
                        client.sendEvent("GET_ABSENCES", absenceDAOList);

                        return;

                    }
                }

                client.sendEvent("BAD_REQUEST");
            }

        } else {
            // Disconnect User
            client.disconnect();
        }
    }

    /***
     * Handle getting absence types logic
     *
     * @param client
     */
    private void getAbsenceTypes(SocketIOClient client) {

        if(absenceTypeList == null) {
            absenceTypeList = absenceTypesRepository.findAll();
        }

        client.sendEvent("GET_ABSENCE_TYPES", absenceTypeList);
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

            Absence absence = absences.get(i);
            AbsenceDAO newAbsenceDAO = new AbsenceDAO();

            newAbsenceDAO.setAbsenceId(absence.getAbsenceId());
            newAbsenceDAO.setAbsenceType(absence.getAbsenceType().getName());
            newAbsenceDAO.setDate(absence.getDate());
            newAbsenceDAO.setJustificable(absence.isJustificable());
            newAbsenceDAO.setJustified(absence.isJustified());
            newAbsenceDAO.setLessonId(absence.getLesson().getLessonId());
            newAbsenceDAO.setDiscipline(absence.getDiscipline().getName());
            newAbsenceDAO.setStudentId(absence.getStudent().getStudentId());
            newAbsenceDAO.setRecuperated(absence.isRecuperated());

            absenceDAOList.add(newAbsenceDAO);
        }

        return absenceDAOList;
    }

    /***
     * Helper to convert Absence into AbsenceDAO
     *
     * @param absence
     * @return
     */
    private AbsenceDAO populateDTA(Absence absence) {

        AbsenceDAO absenceDAO = new AbsenceDAO();

        absenceDAO.setStudentId(absence.getStudent().getStudentId());
        absenceDAO.setAbsenceId(absence.getAbsenceId());
        absenceDAO.setLessonId(absence.getLesson().getLessonId());
        absenceDAO.setAbsenceType(absence.getAbsenceType().getName());
        absenceDAO.setDate(absence.getDate());
        absenceDAO.setDiscipline(absence.getDiscipline().getName());
        absenceDAO.setJustificable(absence.isJustificable());
        absenceDAO.setJustified(absence.isJustified());
        absenceDAO.setRecuperated(absence.isRecuperated());

        return absenceDAO;
    }

    /***
     * Helper to get student absences
     *
     * @param student
     * @return
     */
    private List<Absence> retrieveAbsences(Student student) {

        // Find absences
        Optional<List<Absence>> foundAbsences = absencesRepository.findAllByStudent(student);

        // If found
        if(foundAbsences.isPresent()) {

            // return them
            return foundAbsences.get();

        } else {
            return null;
        }

    }

    /***
     * Helper to convert MarkAbsence into Absence
     *
     * @param markAbsence
     * @return
     */
    private Absence populateAbsence(MarkAbsence markAbsence) {

        Absence absence = new Absence();

        AbsenceType  absenceType = getAbsenceType(markAbsence.getAbsenceType());
        Discipline discipline = getDiscipline(markAbsence.getDisciplineId());
        Student student = getStudent(markAbsence.getStudentId());
        Lesson lesson = getLesson(markAbsence.getLessonId());

        if(absenceType != null && discipline != null && student != null && lesson != null) {

            absence.setJustificable(markAbsence.isJustificable());
            absence.setAbsenceType(absenceType);
            absence.setDiscipline(discipline);
            absence.setStudent(student);
            absence.setLesson(lesson);
            absence.setDate(new Date());

            return absence;
        } else {
            return null;
        }
    }

    /***
     * Helper to get Lesson from lessonId
     *
     * @param lessonId
     * @return
     */
    private Lesson getLesson(long lessonId) {

        Optional<Lesson> foundLesson = lessonsRepository.findById(lessonId);

        if(foundLesson.isPresent()) {
            return foundLesson.get();
        } else {
            return null;
        }
    }

    /***
     * Helper to get Student from studentId
     *
     * @param studentId
     * @return
     */
    private Student getStudent(String studentId) {

        Optional<Student> foundStudent = studentsRepository.findById(studentId);

        if(foundStudent.isPresent()) {
            return foundStudent.get();
        } else {
            return null;
        }
    }

    /***
     * Helper to get Discipline from disciplineId
     *
     * @param disciplineId
     * @return
     */
    private Discipline getDiscipline(long disciplineId) {

        Optional<Discipline> foundDiscipline = disciplinesRepository.findById(disciplineId);

        if(foundDiscipline.isPresent()) {
            return foundDiscipline.get();
        } else {
            return null;
        }
    }

    /***
     * Helper to get AbsenceType from absenceTypeId
     *
     * @param absenceTypeId
     * @return
     */
    private AbsenceType getAbsenceType(long absenceTypeId) {

        Optional<AbsenceType> foundAbsenceType = absenceTypesRepository.findById(absenceTypeId);

        if(foundAbsenceType.isPresent()) {
            return foundAbsenceType.get();
        }  else {
            return null;
        }
    }
}
