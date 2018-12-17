package pt.lisomatrix.Sockets.modules;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.repositories.DisciplinesRepository;
import pt.lisomatrix.Sockets.repositories.TeachersRepository;

import java.util.List;
import java.util.Optional;

/***
 * Listens and handles all WebSocket messages related to Disciplines
 *
 */
@Component
public class DisciplinesModule {

    /***
     * WebSockets server object
     */
    private SocketIOServer server;

    /***
     * REDIS Users Storage repository to get in ram info
     */
    private RedisUsersStorageRepository redisUsersStorageRepository;

    /***
     * Disciplinary Absences Repository to save and update
     */
    private DisciplinesRepository disciplinesRepository;

    /***
     * Teachers Repository to get info
     */
    private TeachersRepository teachersRepository;

    /***
     * Initialize all class dependencies and
     * Start listening to specific WebSocket messages
     *
     * @param server
     * @param redisUsersStorageRepository
     * @param disciplinesRepository
     * @param teachersRepository
     */
    @Autowired
    public DisciplinesModule(SocketIOServer server, RedisUsersStorageRepository redisUsersStorageRepository, DisciplinesRepository disciplinesRepository, TeachersRepository teachersRepository) {
        this.server = server;
        this.redisUsersStorageRepository = redisUsersStorageRepository;
        this.disciplinesRepository = disciplinesRepository;
        this.teachersRepository = teachersRepository;

        // Listen for the following events
        this.server.addEventListener("GET_DISCIPLINES", Discipline.class, new DataListener<Discipline>() {
            @Override
            public void onData(SocketIOClient client, Discipline discipline, AckRequest ackRequest) throws Exception {
                getDisciplines(client, ackRequest, client.getSessionId().toString());
            }
        });

        this.server.addEventListener("GET_TEACHER_DISCIPLINES", Discipline.class, new DataListener<Discipline>() {
            @Override
            public void onData(SocketIOClient client, Discipline discipline, AckRequest ackRequest) throws Exception {
                getTeacherDisciplines(client, ackRequest, client.getSessionId().toString());
            }
        });
    }

    /***
     * Handle getting Teacher Disciplines logic
     *
     * @param client
     * @param ackRequest
     * @param sessionId
     */
    private void getTeacherDisciplines(SocketIOClient client, AckRequest ackRequest, String sessionId) {

        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        if(foundRedisUserStorage.isPresent()) {

            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            if(redisUserStorage.getRole().equals(Roles.PROFESSOR.toString())) {

                Optional<List<Discipline>> foundDisciplines = disciplinesRepository.findAllByTeachersIsIn(redisUserStorage.getTeacher());

                if(foundDisciplines.isPresent()) {

                    client.sendEvent("GET_TEACHER_DISCIPLINES", foundDisciplines.get());
                }
            }

        } else {
            client.disconnect();
        }
    }

    /***
     * Handle getting Disciplines logic
     *
     * @param client
     * @param ackRequest
     * @param sessionId
     */
    private void getDisciplines(SocketIOClient client, AckRequest ackRequest, String sessionId) {

        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        if(foundRedisUserStorage.isPresent()){

            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            List<Discipline> disciplines = disciplinesRepository.findAll();

            client.sendEvent("GET_DISCIPLINES", disciplines);

        } else {
            client.disconnect();
        }

    }
}
