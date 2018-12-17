package pt.lisomatrix.Sockets.modules;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.websocket.models.ClassDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/***
 * Listens and handles all WebSocket messages related to Classes
 */
@Component
public class ClassesModule {

    /***
     * WebSockets server object
     */
    private SocketIOServer server;

    /***
     * REDIS Users Storage repository to get in ram info
     */
    private RedisUsersStorageRepository redisUsersStorageRepository;

    /***
     * Classes Repository to save and update
     */
    private ClassesRepository classesRepository;

    /***
     * Initialize all class dependencies and
     * Start listening to specific WebSocket messages
     *
     * @param server
     * @param redisUsersStorageRepository
     * @param classesRepository
     */
    @Autowired
    public ClassesModule(SocketIOServer server, RedisUsersStorageRepository redisUsersStorageRepository, ClassesRepository classesRepository) {
        this.server = server;
        this.redisUsersStorageRepository = redisUsersStorageRepository;
        this.classesRepository = classesRepository;

        // Listen for the following events
        this.server.addEventListener("GET_CLASSES", Class.class, new DataListener<Class>() {
            @Override
            public void onData(SocketIOClient client, Class receivedClass, AckRequest ackRequest) throws Exception {
                getClasses(client, ackRequest, client.getSessionId().toString());
            }
        });
    }

    /***
     * Handle getting classes logic
     *
     * @param client
     * @param ackRequest
     * @param sessionId
     */
    private void getClasses(SocketIOClient client, AckRequest ackRequest, String sessionId) {

        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        if(foundRedisUserStorage.isPresent()) {

            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            if(redisUserStorage.getRole().equals(Roles.PROFESSOR.toString())) {

                List<Class> classes = getClassesByTeacher(redisUserStorage.getTeacher());

                if(classes != null) {

                    List<ClassDAO> classDAOList = populateDTAList(classes);

                    client.sendEvent("GET_CLASSES", classDAOList);

                } else {

                    client.sendEvent("GET_CLASSES");

                }

            } else {

                client.sendEvent("UNAUTHORIZED");
            }

        } else {
            client.disconnect();
        }
    }

    /***
     * Helper to get all classes related to a Teacher
     *
     * @param teacher
     * @return
     */
    private List<Class> getClassesByTeacher(Teacher teacher) {

        Optional<List<Class>> foundClasses = classesRepository.findAllByTeachersIsIn(teacher);

        if(foundClasses.isPresent()) {
            return foundClasses.get();
        }

        return null;
    }

    /***
     * Helper to convert a List<Class> into List<ClassDAO>
     *
     * @param classes
     * @return
     */
    private List<ClassDAO> populateDTAList(List<Class> classes) {

        List<ClassDAO> classDAOList = new ArrayList<ClassDAO>();

        for(int i = 0; i < classes.size(); i++) {

            Class listClass = classes.get(i);
            ClassDAO classDAO = new ClassDAO();

            classDAO.setClassDirectorId(listClass.getClassDirector().getTeacherId());
            classDAO.setClassDirectorName(listClass.getClassDirector().getName());
            classDAO.setClassId(listClass.getClassId());
            classDAO.setCourse(listClass.getCourse().getName());
            classDAO.setName(listClass.getName());

            classDAOList.add(classDAO);
        }

        return classDAOList;
    }
}
