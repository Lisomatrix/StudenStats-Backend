package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Module;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.ModulesRepository;
import pt.lisomatrix.Sockets.requests.models.GetModules;
import pt.lisomatrix.Sockets.requests.models.NewModule;
import pt.lisomatrix.Sockets.websocket.models.Event;
import pt.lisomatrix.Sockets.websocket.models.ModuleDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ModulesRestController {

    @Autowired
    private ModulesRepository modulesRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @PostMapping("/module")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public ModuleDAO addNewModule(@RequestBody NewModule newModule) {

        Module module = new Module();

        module.setName(newModule.getName());
        module.setDiscipline(new Discipline(newModule.getDisciplineId()));

        Module addedModule = modulesRepository.save(module);

        ModuleDAO moduleDAO = new ModuleDAO();

        moduleDAO.populate(addedModule);

        return moduleDAO;
    }

    @GetMapping("/discipline/{disciplineId}/module")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasAnyRole('ROLE_ALUNO')")
    @CrossOrigin
    public List<ModuleDAO> getModuleByDiscipline(@PathVariable("disciplineId") long disciplineId) {

        Optional<List<Module>> foundModules = modulesRepository.findAllByDiscipline(new Discipline(disciplineId));

        if(foundModules.isPresent()) {

            List<ModuleDAO> moduleDAOList = populateModuleDAOList(foundModules.get());

            return moduleDAOList;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Discipline not found");
    }

    @GetMapping("/class/{classId}/module")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasAnyRole('ROLE_ALUNO')")
    @CrossOrigin
    public List<ModuleDAO> getModulesByClass(@PathVariable("classId") long classId) {

        Optional<Class> foundClass = classesRepository.findById(classId);

        if(foundClass.isPresent()) {

            List<ModuleDAO> moduleDAOList = populateModuleDAOList(foundClass.get().getModules());

            return moduleDAOList;

        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Class not found");
        }
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
