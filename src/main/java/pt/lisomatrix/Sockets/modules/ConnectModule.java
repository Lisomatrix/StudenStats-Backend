package pt.lisomatrix.Sockets.modules;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.*;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.redis.models.RedisToken;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;
import pt.lisomatrix.Sockets.redis.repositories.RedisTokenRepository;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.repositories.*;
import pt.lisomatrix.Sockets.websocket.models.UserDAO;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/***
 * Listens and handles all WebSocket messages related to post authentication
 *
 * This Module takes care for initializing the storage for the user
 */
@Component
public class ConnectModule {

    /***
     * WebSockets server object
     */
    private SocketIOServer server;

    /***
     * REDIS Users Storage repository to get in ram info
     */
    private RedisUsersStorageRepository redisUsersStorageRepository;

    /***
     * Users Repository to get info
     */
    private UsersRepository usersRepository;

    /***
     * REDIS Authentication Tokens Repository to get info and update
     */
    private RedisTokenRepository redisTokenRepository;

    /***
     * Student Repository to get info
     */
    private StudentsRepository studentsRepository;

    /***
     * Teachers Repository to get info
     */
    private TeachersRepository teachersRepository;

    /***
     * Parents Repository to get info
     */
    private ParentsRepository parentsRepository;

    /***
     * Absences Repository to get info
     */
    private AbsencesRepository absencesRepository;

    /***
     * Grades Repository to get info
     */
    private GradesRepository gradesRepository;

    /***
     * Classes Repository to get info
     */
    private ClassesRepository classesRepository;

    /***
     * Disciplines Repository to get info
     */
    private DisciplinesRepository disciplinesRepository;

    /***
     * User Settings Repositoru to get info
     */
    private UserSettingsRepository userSettingsRepository;

    /***
     * Initialize all class dependencies and
     * Start listening to specific WebSocket messages
     *
     * @param server
     * @param redisUsersStorageRepository
     * @param usersRepository
     * @param redisTokenRepository
     * @param studentsRepository
     * @param teachersRepository
     * @param parentsRepository
     * @param absencesRepository
     * @param gradesRepository
     * @param classesRepository
     * @param disciplinesRepository
     * @param userSettingsRepository
     */
    public ConnectModule(SocketIOServer server, RedisUsersStorageRepository redisUsersStorageRepository,
                         UsersRepository usersRepository, RedisTokenRepository redisTokenRepository,
                         StudentsRepository studentsRepository, TeachersRepository teachersRepository,
                         ParentsRepository parentsRepository, AbsencesRepository absencesRepository,
                         GradesRepository gradesRepository, ClassesRepository classesRepository,
                         DisciplinesRepository disciplinesRepository, UserSettingsRepository userSettingsRepository) {

        this.server = server;
        this.redisUsersStorageRepository = redisUsersStorageRepository;
        this.usersRepository = usersRepository;
        this.redisTokenRepository = redisTokenRepository;
        this.studentsRepository = studentsRepository;
        this.teachersRepository = teachersRepository;
        this.parentsRepository = parentsRepository;
        this.absencesRepository = absencesRepository;
        this.gradesRepository = gradesRepository;
        this.classesRepository = classesRepository;
        this.disciplinesRepository = disciplinesRepository;
        this.userSettingsRepository = userSettingsRepository;

        // Listen for the following events
        this.server.addEventListener("SET_STORAGE", Absence.class, new DataListener<Absence>() {
            @Override
            public void onData(final SocketIOClient client, Absence data, final AckRequest ackRequest) {
                setStorage(client, ackRequest, client.getSessionId().toString());
            }
        });
    }

    /***
     * Handle the storage creation logic
     *
     * @param client
     * @param request
     * @param sessionId
     */
    public void setStorage(SocketIOClient client, AckRequest request, String sessionId) {

        // Get session token
        String userToken = client.getHandshakeData().getSingleUrlParam("token");

        // Get token object
        RedisToken redisToken = getToken(userToken, client);

        // Get User
        User user = getUser(redisToken, client);

        // Get current date
        Date actualDate = new Date();

        // Get last session redis user storage
        RedisUserStorage lastRedisUserStorage = getLastSession(user.getUserId());

        // If found update status and reuse session
        if(lastRedisUserStorage != null) {

            lastRedisUserStorage.setDisconnected(false);
            lastRedisUserStorage.setDate(actualDate.toString());
            lastRedisUserStorage.setSessionId(client.getSessionId().toString());

            redisUsersStorageRepository.save(lastRedisUserStorage);

            UserDAO userDAO = new UserDAO();

            userDAO.setClassDirector(null);
            userDAO.setUserThemeSettings(lastRedisUserStorage.getUserSettings().getTheme());

            if(user.getRole().getRole().equals(Roles.ALUNO.toString())) {

                userDAO.setId(lastRedisUserStorage.getStudent().getStudentId());
                userDAO.setName(lastRedisUserStorage.getStudent().getName());
                userDAO.setRoleEntityId(lastRedisUserStorage.getStudent().getStudentId());

            } else if(user.getRole().getRole().equals(Roles.PROFESSOR.toString())) {

                if(lastRedisUserStorage.getTeacherClass() != null) {
                    userDAO.setClassDirector(true);
                }

                userDAO.setId(lastRedisUserStorage.getTeacher().getTeacherId());
                userDAO.setName(lastRedisUserStorage.getTeacher().getName());
                userDAO.setRoleEntityId(lastRedisUserStorage.getTeacher().getTeacherId());

            } else if(user.getRole().getRole().equals(Roles.PARENTE.toString())) {

                userDAO.setId(lastRedisUserStorage.getParent().getParentId());
                userDAO.setName(lastRedisUserStorage.getParent().getName());
                userDAO.setRoleEntityId(lastRedisUserStorage.getParent().getParentId());
            }

            userDAO.setUserId(lastRedisUserStorage.getUserId());

            client.sendEvent("LAST_SESSION_REUSE");
            client.sendEvent("READY", userDAO);

            return;
        }

        // Create and populate redis user storage
        RedisUserStorage redisUserStorage = new RedisUserStorage();

        redisUserStorage.setRole(user.getRole().getRole());
        redisUserStorage.setSessionId(sessionId);
        redisUserStorage.setUser(user);
        redisUserStorage.setDate(actualDate.toString());
        redisUserStorage.setDisconnected(false);
        redisUserStorage.setUserId(user.getUserId());
        redisUserStorage.setUserSettings(getUserSettings(user));

        // Create and populate user
        UserDAO userDAO = new UserDAO();

        userDAO.setUserId(user.getUserId());
        userDAO.setClassDirector(null);
        userDAO.setUserThemeSettings(redisUserStorage.getUserSettings().getTheme());

        // Get user info depending on the role
        // Populate with data that will be very probably be used right away
        if(user.getRole().getRole().equals(Roles.ALUNO.toString())) {

            Student student = getStudent(user);

            redisUserStorage.setStudent(student);
            //redisUserStorage.setAbsences(getAbsences(student)); TODO ERROR ON GETTING THIS LIST BACK
            redisUserStorage.setUserClass(getStudentClass(student));
            redisUserStorage.setGrades(getStudentGrades(student));

            userDAO.setName(student.getName());
            userDAO.setId(student.getStudentId());
            userDAO.setRoleEntityId(student.getStudentId());

        } else if(user.getRole().getRole().equals(Roles.PROFESSOR.toString())) {

            Teacher teacher = getTeacher(user);

            Optional<Class> foundClass = classesRepository.findFirstByClassDirector(teacher);

            if(foundClass.isPresent()) {
                redisUserStorage.setTeacherClass(foundClass.get());
                userDAO.setClassDirector(true);
            }

            redisUserStorage.setTeacher(teacher);
            redisUserStorage.setClasses(getTeacherClasses(teacher));
            redisUserStorage.setTeacherDisciplines(getTeacherDisciplines(teacher));

            userDAO.setName(teacher.getName());
            userDAO.setId(teacher.getTeacherId());
            userDAO.setRoleEntityId(teacher.getTeacherId());

        } else if(user.getRole().getRole().equals(Roles.PARENTE.toString())) {

            Parent parent = getParent(user);
            redisUserStorage.setParent(parent);

            userDAO.setName(parent.getName());
            userDAO.setId(parent.getParentId());
            userDAO.setRoleEntityId(parent.getParentId());
        }

        // Save the user storage on ram
        redisUsersStorageRepository.save(redisUserStorage);
        // Warn the client that he can make request at will
        client.sendEvent("READY", userDAO);

    }

    /***
     * Helper to get User Settings
     *
     * @param user
     * @return
     */
    private UserSettings getUserSettings(User user) {

        Optional<UserSettings> foundUserSettings = userSettingsRepository.findFirstByUser(user);

        if(foundUserSettings.isPresent()) {
            return foundUserSettings.get();
        }

        return null;
    }

    /***
     * Helper to get all disciplines related to a Teacher
     *
     * @param teacher
     * @return
     */
    private List<Discipline> getTeacherDisciplines(Teacher teacher) {

        Optional<List<Discipline>> foundDisciplines = disciplinesRepository.findAllByTeachersIsIn(teacher);

        if(foundDisciplines.isPresent()) {

            return foundDisciplines.get();
        }

        return null;
    }

    /***
     * Helper to get all classes related to a Teacher
     *
     * @param teacher
     * @return
     */
    private List<Class> getTeacherClasses(Teacher teacher) {

        Optional<List<Class>> foundClasses = classesRepository.findAllByTeachersIsIn(teacher);

        if(foundClasses.isPresent()) {

            return foundClasses.get();
        }

        return null;
    }

    /***
     * Helper to get the Student Class
     *
     * @param student
     * @return
     */
    private Class getStudentClass(Student student) {

        Optional<Class> foundClass = classesRepository.findFirstByStudents(student);

        if(foundClass.isPresent()) {

            foundClass.get();
        }

        return null;
    }

    /***
     * Helper to get all Student grades
     *
     * @param student
     * @return
     */
    private List<Grade> getStudentGrades(Student student) {

        Optional<List<Grade>> foundGrades = gradesRepository.findAllByStudentIsIn(student);

        if(foundGrades.isPresent()) {

            return foundGrades.get();
        }

        return null;
    }

    /***
     * Helper to Get last session/user storage with this userId
     *
     * @param userId
     * @return
     */
    private RedisUserStorage getLastSession(Long userId) {

        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findFirstByUserIdAndDisconnected(userId, true);

        if(foundRedisUserStorage.isPresent()) {
            return foundRedisUserStorage.get();
        }

        Optional<List<RedisUserStorage>> foundLastSessionStorages = redisUsersStorageRepository.findAllByDisconnected(true);

        Iterable<RedisUserStorage> all = redisUsersStorageRepository.findAll();

        if(foundLastSessionStorages.isPresent()) {

            //List<RedisUserStorage> lastSessionStorage = foundLastSessionStorages.get();

            List<RedisUserStorage> lastSessionStorage = (List<RedisUserStorage>) all;

            for(int i = 0; i < lastSessionStorage.size(); i++) {

                RedisUserStorage temp = lastSessionStorage.get(i);

                if(temp.getUserId().equals(userId)) {
                    return  temp;
                }

                System.out.println(lastSessionStorage.get(i).getUserId());


            }
        }

        return null;
    }

    /***
     * Helper to Get all Student absences
     *
     * @param student
     * @return
     */
    private List<Absence> getAbsences(Student student) {

        Optional<List<Absence>> foundAbsences = absencesRepository.findAllByStudent(student);

        if(foundAbsences.isPresent()) {

            return foundAbsences.get();
        }

        return null;
    }

    /***
     * Helper to get Parent by User
     *
     * @param user
     * @return
     */
    private Parent getParent(User user) {

        Optional<Parent> foundParent = parentsRepository.findFirstByUser(user);

        if(foundParent.isPresent()) {

            return foundParent.get();

        } else {
            return null;
        }
    }

    /***
     * Helper to get Teacher by User
     *
     * @param user
     * @return
     */
    private Teacher getTeacher(User user) {
        Optional<Teacher> foundTeacher = teachersRepository.findFirstByUser(user);

        if(foundTeacher.isPresent()) {
            return foundTeacher.get();
        } else {
            return null;
        }
    }

    /***
     * Helper to get Student by User
     *
     * @param user
     * @return
     */
    private Student getStudent(User user) {
        Optional<Student> foundStudent = studentsRepository.findFirstByUser(user);

        if(foundStudent.isPresent()) {

            return foundStudent.get();

        } else {
            return null;
        }
    }

    /***
     * Helper to get User by Authentication Token
     *
     * If not found Disconnect WebSocket
     *
     * @param redisToken
     * @param client
     * @return
     */
    private User getUser(RedisToken redisToken, SocketIOClient client) {

        Optional<User> foundUser = usersRepository.findById(redisToken.getUserId());

        if(foundUser.isPresent()) {

            return foundUser.get();

        } else {
            client.disconnect();
            return null;
        }
    }

    /***
     * Helper to get Authentication Token Object
     *
     * If not found Disconnect WebSocket
     *
     * @param token
     * @param client
     * @return
     */
    private RedisToken getToken(String token, SocketIOClient client) {

        Optional<RedisToken> foundRedisToken = redisTokenRepository.findById(token);

        if(foundRedisToken.isPresent()) {

            return foundRedisToken.get();
        } else {
            client.disconnect();
            return null;
        }
    }
}
