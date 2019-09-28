package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Module;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.DisciplinesRepository;
import pt.lisomatrix.Sockets.repositories.ModulesRepository;
import pt.lisomatrix.Sockets.requests.models.NewModule;
import pt.lisomatrix.Sockets.response.models.ModuleResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ModulesRestController {

    @Autowired
    private ModulesRepository modulesRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private DisciplinesRepository disciplinesRepository;

    @PutMapping("/module/{moduleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ModuleResponse updateModule(@PathVariable long moduleId, @RequestBody NewModule newModule) {
        Optional<Module> foundModule = modulesRepository.findById(moduleId);

        if(foundModule.isPresent()) {

            Module module = foundModule.get();

            module.setName(newModule.getName());
            module.setHours(newModule.getHours());

            if(!module.getDiscipline().getDisciplineId().equals(newModule.getDisciplineId())) {

                Optional<Discipline> foundDiscipline = disciplinesRepository.findById(newModule.getDisciplineId());

                if(foundDiscipline.isPresent()) {

                    module.setDiscipline(foundDiscipline.get());
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found!");
                }
            }

            module = modulesRepository.save(module);

            ModuleResponse moduleResponse = new ModuleResponse();

            moduleResponse.populate(module);

            return moduleResponse;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found!");
    }

    @PostMapping("/module")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ModuleResponse addNewModule(@RequestBody NewModule newModule) {

        Module module = new Module();

        module.setName(newModule.getName());
        module.setHours(newModule.getHours());

        Optional<Discipline> foundDiscipline = disciplinesRepository.findById(newModule.getDisciplineId());

        if(foundDiscipline.isPresent()) {
            module.setDiscipline(foundDiscipline.get());

            Module addedModule = modulesRepository.save(module);

            ModuleResponse moduleResponse = new ModuleResponse();

            moduleResponse.populate(addedModule);

            return moduleResponse;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found!");
    }

    @GetMapping("/discipline/{disciplineId}/module")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasAnyRole('ROLE_ALUNO')")
    @CrossOrigin
    public List<ModuleResponse> getModuleByDiscipline(@PathVariable("disciplineId") long disciplineId) {

        Optional<List<Module>> foundModules = modulesRepository.findAllByDiscipline(new Discipline(disciplineId));

        if(foundModules.isPresent()) {

            List<ModuleResponse> moduleResponseList = populateModuleDAOList(foundModules.get());

            return moduleResponseList;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Discipline not found");
    }

    @GetMapping("/class/{classId}/module")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasAnyRole('ROLE_ALUNO') or hasAnyRole('ROLE_PARENT')")
    @CrossOrigin
    public List<ModuleResponse> getModulesByClass(@PathVariable("classId") long classId) {

        Optional<Class> foundClass = classesRepository.findById(classId);

        if(foundClass.isPresent()) {

            List<ModuleResponse> moduleResponseList = populateModuleDAOList(foundClass.get().getModules());

            return moduleResponseList;

        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Class not found");
        }
    }

    @GetMapping("/module")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public List<ModuleResponse> getModules() {
        return populateModuleDAOList(modulesRepository.findAll());
    }

    private List<ModuleResponse> populateModuleDAOList(List<Module> modules) {

        List<ModuleResponse> moduleResponses = new ArrayList<>();

        for(int i = 0; i < modules.size(); i++) {

            Module module = modules.get(i);

            ModuleResponse moduleResponse = new ModuleResponse();

            moduleResponse.populate(module);

            moduleResponses.add(moduleResponse);
        }

        return moduleResponses;
    }
}
