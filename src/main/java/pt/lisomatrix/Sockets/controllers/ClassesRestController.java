package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.models.User;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.TeachersRepository;
import pt.lisomatrix.Sockets.repositories.UsersRepository;
import pt.lisomatrix.Sockets.requests.models.Response;
import pt.lisomatrix.Sockets.websocket.models.ClassDAO;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
public class ClassesRestController {

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TeachersRepository teachersRepository;

    @GetMapping("/class")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<ClassDAO> getClasses(Principal principal) {

        Optional<List<Class>> foundClasses = classesRepository.findAllByTeachersUserIdIsIn(Long.parseLong(principal.getName()));

        if(foundClasses.isPresent()) {

            List<ClassDAO> classDAOList = populateDTAList(foundClasses.get());

            return classDAOList;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Classes not found");
    }

    @GetMapping("/class/async")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public DeferredResult<ResponseEntity<?>> getClassesAsync(Principal principal) throws Exception {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        CompletableFuture.supplyAsync(() -> classesRepository.findAllByTeachersUserIdIsIn(Long.parseLong(principal.getName())))
                .thenApplyAsync((foundClasses) -> {

                    if(foundClasses.isPresent()) {

                        deferredResult.setResult(ResponseEntity.ok(populateDTAList(foundClasses.get())));
                        return null;

                    } else {
                        return null;
                    }
                });

        return deferredResult;
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
            ClassDAO classDAO = new ClassDAO();

            classDAO.populate(classes.get(i));

            classDAOList.add(classDAO);
        }

        return classDAOList;
    }
}
