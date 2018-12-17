package pt.lisomatrix.Sockets.modules;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.repositories.AbsencesRepository;
import pt.lisomatrix.Sockets.repositories.HoursRecuperationRepository;

@Component
public class HoursRecuperationModule {

    /***
     * WebSockets server object
     */
    private SocketIOServer server;

    /***
     * REDIS Users Storage repository to get in ram info
     */
    private RedisUsersStorageRepository redisUsersStorageRepository;

    /***
     * Absences Repository to save and update
     */
    private AbsencesRepository absencesRepository;

    /***
     *  Hours Recuperations Repository to save and update
     */
    private HoursRecuperationRepository hoursRecuperationRepository;

    /***
     * Initialize all class dependencies and
     * Start listening to specific WebSocket messages
     *
     * @param server
     * @param redisUsersStorageRepository
     * @param hoursRecuperationModule
     * @param absencesRepository
     */
    @Autowired
    public HoursRecuperationModule(SocketIOServer server, RedisUsersStorageRepository redisUsersStorageRepository,
                                   HoursRecuperationRepository hoursRecuperationRepository, AbsencesRepository absencesRepository) {
        this.server = server;
        this.redisUsersStorageRepository = redisUsersStorageRepository;
        this.absencesRepository = absencesRepository;
        this.hoursRecuperationRepository = hoursRecuperationRepository;
    }

    private void recuperateHours(SocketIOClient client, AckRequest ackRequest) {

    }
}
