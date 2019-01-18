package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.util.SessionHandler;
import pt.lisomatrix.Sockets.websocket.models.ClassDAO;
import pt.lisomatrix.Sockets.websocket.models.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ClassesController {

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private SessionHandler sessionHandler;

    @MessageMapping("/class")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Event getClasses(StompHeaderAccessor accessor) {

        Teacher teacher = (Teacher) sessionHandler.getAttribute(accessor.getSessionId(), "teacher");

        Optional<List<Class>> foundClasses = classesRepository.findAllByTeachersIsIn(teacher);

        if(foundClasses.isPresent()) {

            List<ClassDAO> classDAOList = populateDTAList(foundClasses.get());

            return new Event("GET_CLASS", classDAOList);
        }

        return new Event("BAD_REQUEST", null);
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
