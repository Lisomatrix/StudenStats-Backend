package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Module;
import pt.lisomatrix.Sockets.repositories.ModulesRepository;
import pt.lisomatrix.Sockets.requests.models.GetModules;
import pt.lisomatrix.Sockets.requests.models.NewModule;
import pt.lisomatrix.Sockets.util.SessionHandler;
import pt.lisomatrix.Sockets.websocket.models.Event;
import pt.lisomatrix.Sockets.websocket.models.ModuleDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ModulesController {

    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private ModulesRepository modulesRepository;

    @MessageMapping("/modules/create")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Event addNewModule(NewModule newModule, StompHeaderAccessor accessor) {

        Module module = new Module();

        module.setName(newModule.getName());
        module.setDiscipline(new Discipline(newModule.getDisciplineId()));

        Module addedModule = modulesRepository.save(module);

        ModuleDAO moduleDAO = new ModuleDAO();

        moduleDAO.populate(addedModule);

        return new Event("NEW_MODULE", moduleDAO);
    }

    @MessageMapping("/discipline/modules")
    @SendToUser("/queue/reply")
    public Event getModuleByDiscipline(GetModules getModules) {

        Optional<List<Module>> foundModules = modulesRepository.findAllByDiscipline(new Discipline(getModules.getDisciplineId()));

        if(foundModules.isPresent()) {

            List<ModuleDAO> moduleDAOList = populateModuleDAOList(foundModules.get());

            return new Event("GET_MODULES", moduleDAOList);
        }

        return new Event("BAD_REQUEST", null);
    }

    @MessageMapping("/modules")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasAnyRole('ROLE_ALUNO')")
    public Event getModules(StompHeaderAccessor accessor) {

        Class aClass = (Class) sessionHandler.getAttribute(accessor.getSessionId(), "class");

        if(aClass == null) {
            return new Event("BAD_REQUEST", null);
        }

        List<ModuleDAO> moduleDAOList = populateModuleDAOList(aClass.getModules());

        return new Event("GET_MODULES", moduleDAOList);
    }

    private List<ModuleDAO> populateModuleDAOList(List<Module> modules) {

        List<ModuleDAO> moduleDAOS = new ArrayList<>();

        for(int i = 0; i < modules.size(); i++) {

            Module module = modules.get(i);

            ModuleDAO moduleDAO = new ModuleDAO();

            moduleDAO.setDisciplineId(module.getDiscipline().getDisciplineId());
            moduleDAO.setModuleId(module.getModuleId());
            moduleDAO.setName(module.getName());

            moduleDAOS.add(moduleDAO);
        }

        return moduleDAOS;
    }
}
