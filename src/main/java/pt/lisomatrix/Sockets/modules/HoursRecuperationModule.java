package pt.lisomatrix.Sockets.modules;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.Absence;
import pt.lisomatrix.Sockets.models.HourRecuperation;
import pt.lisomatrix.Sockets.models.Lesson;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.repositories.AbsencesRepository;
import pt.lisomatrix.Sockets.repositories.HoursRecuperationRepository;
import pt.lisomatrix.Sockets.repositories.LessonsRepository;
import pt.lisomatrix.Sockets.repositories.StudentsRepository;
import pt.lisomatrix.Sockets.requests.models.RecuperateAbsence;
import pt.lisomatrix.Sockets.websocket.models.AbsenceDAO;
import sun.util.resources.cldr.ext.LocaleNames_en_GB;

import java.util.*;

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

    private LessonsRepository lessonsRepository;

    private StudentsRepository studentsRepository;


    /***
     * Initialize all class dependencies and
     * Start listening to specific WebSocket messages
     *
     * @param server
     * @param redisUsersStorageRepository
     * @param hoursRecuperationRepository
     * @param absencesRepository
     */
    @Autowired
    public HoursRecuperationModule(SocketIOServer server, RedisUsersStorageRepository redisUsersStorageRepository,
                                   HoursRecuperationRepository hoursRecuperationRepository, AbsencesRepository absencesRepository,
                                   LessonsRepository lessonsRepository, StudentsRepository studentsRepository) {
        this.server = server;
        this.redisUsersStorageRepository = redisUsersStorageRepository;
        this.absencesRepository = absencesRepository;
        this.hoursRecuperationRepository = hoursRecuperationRepository;
        this.lessonsRepository = lessonsRepository;
        this.studentsRepository = studentsRepository;

        this.server.addEventListener("HOUR_RECUPERATE", RecuperateAbsence.class, new DataListener<RecuperateAbsence>() {
            @Override
            public void onData(SocketIOClient client, RecuperateAbsence data, AckRequest ackSender) throws Exception {
                recuperateHours(client, ackSender, client.getSessionId().toString(), data);
            }
        });
    }

    private void recuperateHours(SocketIOClient client, AckRequest ackRequest, String sessionId, RecuperateAbsence recuperateAbsence) {

        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        if(foundRedisUserStorage.isPresent()) {

            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            if(recuperateAbsence != null) {

                List<Long> ids = new ArrayList<>();

                for(int i = 0; i < recuperateAbsence.getAbsenceDAOS().size(); i++) {
                    ids.add(recuperateAbsence.getAbsenceDAOS().get(i).getAbsenceId());
                }

                List<Absence> foundAbsences = absencesRepository.findAllById(ids);

                for(int i = 0; i < foundAbsences.size(); i++) {
                    foundAbsences.get(i).setRecuperated(true);
                }

                HourRecuperation hourRecuperation = new HourRecuperation();

                hourRecuperation.setAbsences(foundAbsences);
                hourRecuperation.setDate(new Date());

                hoursRecuperationRepository.save(hourRecuperation);

                client.sendEvent("HOUR_RECUPERATED");

            } else {
                client.sendEvent("BAD_REQUEST");
            }

        } else {
            client.disconnect();
        }
    }
}
