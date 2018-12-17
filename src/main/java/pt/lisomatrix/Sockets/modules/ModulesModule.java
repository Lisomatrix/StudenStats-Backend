package pt.lisomatrix.Sockets.modules;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;
import pt.lisomatrix.Sockets.models.Module;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.repositories.DisciplinesRepository;
import pt.lisomatrix.Sockets.repositories.ModulesRepository;
import pt.lisomatrix.Sockets.requests.models.GetModules;
import pt.lisomatrix.Sockets.websocket.models.ModuleDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ModulesModule {

    private SocketIOServer server;

    private RedisUsersStorageRepository redisUsersStorageRepository;

    private ModulesRepository modulesRepository;

    private DisciplinesRepository disciplinesRepository;

    public ModulesModule(SocketIOServer server, RedisUsersStorageRepository redisUsersStorageRepository,
                         ModulesRepository modulesRepository, DisciplinesRepository disciplinesRepository) {
        this.server = server;
        this.redisUsersStorageRepository = redisUsersStorageRepository;
        this.modulesRepository = modulesRepository;
        this.disciplinesRepository = disciplinesRepository;

        this.server.addEventListener("GET_MODULE_DISCIPLINE", GetModules.class, new DataListener<GetModules>() {
            @Override
            public void onData(SocketIOClient client, GetModules data, AckRequest ackSender) throws Exception {
                getModuleByDiscipline(client, ackSender, client.getSessionId().toString(), data);
            }
        });
    }

    private void getModuleByDiscipline(SocketIOClient client, AckRequest request, String sessionId, GetModules getModules) {

        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        if(foundRedisUserStorage.isPresent()) {

            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            Optional<Discipline> foundDiscipline = disciplinesRepository.findById(getModules.getDisciplineId());

            if(foundDiscipline.isPresent()) {

                Discipline discipline = foundDiscipline.get();

                Optional<List<Module>> foundModules = modulesRepository.findAllByDiscipline(discipline);

                if(foundModules.isPresent()) {



                } else {
                    client.sendEvent("BAD_REQUEST");
                }

            } else {
                client.sendEvent("BAD_REQUEST");
            }

        } else {
            client.disconnect();
        }
    }

    private List<ModuleDAO> populateDTAList(List<Module> modules) {

        List<ModuleDAO> moduleDAOS = new ArrayList<>();

        for(int i = 0; i < modules.size(); i++) {

            Module module = modules.get(i);

            ModuleDAO moduleDAO = new ModuleDAO();

            moduleDAO.setDisciplineId(module.getDiscipline().getDisciplineId());
            moduleDAO.setModuleId(module.getModuleId());
            moduleDAO.setName(module.getName());

            moduleDAOS.add(moduleDAO);
        }
    }

}
