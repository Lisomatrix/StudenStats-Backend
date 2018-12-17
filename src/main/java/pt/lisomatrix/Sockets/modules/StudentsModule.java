package pt.lisomatrix.Sockets.modules;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.redis.models.RedisStorageToken;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.StudentsRepository;
import pt.lisomatrix.Sockets.requests.models.GetStudents;
import pt.lisomatrix.Sockets.websocket.models.StudentDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/***
 * Listens and handles all WebSocket messages related to students
 *
 */
@Component
public class StudentsModule {

    /***
     * WebSockets server object
     */
    private SocketIOServer server;

    /***
     * REDIS Users Storage repository to get in ram info
     */
    private RedisUsersStorageRepository redisUsersStorageRepository;

    /***
     * Students Repository to get info
     */
    private StudentsRepository studentsRepository;

    /***
     * Classes Repository to get info
     */
    private ClassesRepository classesRepository;

    /***
     * Initialize all class dependencies and
     * Start listening to specific WebSocket messages
     *
     * @param server
     * @param studentsRepository
     * @param classesRepository
     * @param redisUsersStorageRepository
     */
    public StudentsModule(SocketIOServer server, StudentsRepository studentsRepository, ClassesRepository classesRepository, RedisUsersStorageRepository redisUsersStorageRepository) {
        this.server = server;
        this.redisUsersStorageRepository = redisUsersStorageRepository;
        this.studentsRepository = studentsRepository;
        this.classesRepository = classesRepository;

        // Listen for the following events
        this.server.addEventListener("GET_STUDENTS", GetStudents.class, new DataListener<GetStudents>() {
            @Override
            public void onData(SocketIOClient socketIOClient, GetStudents getStudents, AckRequest ackRequest) throws Exception {
                getStudents(socketIOClient, getStudents, ackRequest, socketIOClient.getSessionId().toString());
            }
        });

    }

    /***
     * Handle getting Students logic
     *
     * @param client
     * @param getStudents
     * @param ackRequest
     * @param sessionId
     */
    private void getStudents(SocketIOClient client, GetStudents getStudents, AckRequest ackRequest, String sessionId) {

        if(getStudents == null) {
            client.sendEvent("BAD_REQUEST");
            return;
        }

        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        if(foundRedisUserStorage.isPresent()) {

            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            if(redisUserStorage.getRole().equals(Roles.PROFESSOR.toString())) {

                Optional<Class> foundClass = classesRepository.findById(getStudents.getClassId());

                if(foundClass.isPresent()) {

                    Class userClass = foundClass.get();

                    List<Student> students = userClass.getStudents();

                    List<StudentDAO> studentDAOList = populateStudentDAOList(students, userClass.getClassId());

                    client.sendEvent("GET_STUDENTS", studentDAOList);

                    return;
                }

                client.sendEvent("BAD_REQUEST");
            }

        } else {
            client.disconnect();
        }
    }

    /***
     * Helper to convert List<Student> to List<StudentDAO>
     *
     * @param students
     * @param classId
     * @return
     */
    private List<StudentDAO> populateStudentDAOList(List<Student> students, long classId) {

        List<StudentDAO> studentDAOList = new ArrayList<StudentDAO>();

        for(int i = 0; i < students.size(); i++) {

            Student tempStudent = students.get(i);
            StudentDAO studentDAO = new StudentDAO();

            studentDAO.setName(tempStudent.getName());
            studentDAO.setStudentId(tempStudent.getStudentId());
            studentDAO.setClassId(classId);

            studentDAOList.add(studentDAO);
        }

        return studentDAOList;

    }
}
