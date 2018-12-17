package pt.lisomatrix.Sockets.modules;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.models.Schedule;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.repositories.ScheduleExceptionsRepository;
import pt.lisomatrix.Sockets.repositories.ScheduleHoursRepository;
import pt.lisomatrix.Sockets.repositories.SchedulesRepository;

/***
 * Listens and handles all WebSocket messages related to Schedules
 *
 */
@Component
public class ScheduleModule {

    /***
     * WebSockets server object
     */
    private SocketIOServer server;

    /***
     * REDIS Users Storage repository to get in ram info
     */
    private RedisUsersStorageRepository redisUsersStorageRepository;

    /***
     * Schedule Exceptions Repository to save and update
     */
    private ScheduleExceptionsRepository scheduleExceptionsRepository;

    /***
     * Schedules Repository to save and update
     */
    private SchedulesRepository schedulesRepository;

    /***
     * Schedule Hours Repository to save and update
     */
    private ScheduleHoursRepository scheduleHoursRepository;

    /***
     * Initialize all class dependencies and
     * Start listening to specific WebSocket messages
     *
     * @param server
     * @param redisUsersStorageRepository
     * @param scheduleHoursRepository
     * @param schedulesRepository
     * @param scheduleExceptionsRepository
     */
    @Autowired
    public ScheduleModule(SocketIOServer server, RedisUsersStorageRepository redisUsersStorageRepository, ScheduleHoursRepository scheduleHoursRepository,
                          SchedulesRepository schedulesRepository, ScheduleExceptionsRepository scheduleExceptionsRepository) {

        this.server = server;
        this.redisUsersStorageRepository = redisUsersStorageRepository;
        this.schedulesRepository = schedulesRepository;
        this.scheduleHoursRepository = scheduleHoursRepository;
        this.scheduleExceptionsRepository = scheduleExceptionsRepository;

        this.server.addEventListener("GET_SCHEDULE", Schedule.class, new DataListener<Schedule>() {
            @Override
            public void onData(SocketIOClient socketIOClient, Schedule schedule, AckRequest ackRequest) throws Exception {

            }
        });
    }
}
