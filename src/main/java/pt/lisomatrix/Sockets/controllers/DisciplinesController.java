package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.models.Test;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.DisciplinesRepository;
import pt.lisomatrix.Sockets.repositories.TestsRepository;
import pt.lisomatrix.Sockets.requests.models.MarkTest;
import pt.lisomatrix.Sockets.requests.models.RemoveTest;
import pt.lisomatrix.Sockets.util.SessionHandler;
import pt.lisomatrix.Sockets.websocket.models.Event;
import pt.lisomatrix.Sockets.websocket.models.TestDAO;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
@Component
public class DisciplinesController {

    @Autowired
    private DisciplinesRepository disciplinesRepository;

    @Autowired
    private SessionHandler sessionHandler;

    @MessageMapping("/class/disciplines")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Event getClassDisciplines(StompHeaderAccessor accessor) throws Exception {

        Class aClass = (Class) sessionHandler.getAttribute(accessor.getSessionId(), "class");

        if(aClass == null) {
            return new Event("BAD_REQUEST", null);
        }

        Optional<List<Discipline>> foundDisciplines = disciplinesRepository.findAllByClass(((Class) aClass).getClassId());

        if(foundDisciplines.isPresent()) {

            List<Discipline> disciplines = foundDisciplines.get();

            return new Event("GET_CLASS_DISCIPLINES", disciplines);
        }

        return new Event("BAD_REQUEST", null);
    }

    @MessageMapping("/teacher/disciplines")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Event getTeacherDisciplines(StompHeaderAccessor accessor) {

        Teacher teacher = (Teacher) sessionHandler.getAttribute(accessor.getSessionId(), "teacher");

        Optional<List<Discipline>> foundDiscipline = disciplinesRepository.findAllByTeachersIsIn(teacher);

        if(foundDiscipline.isPresent()) {

            List<Discipline> disciplines = foundDiscipline.get();

            return new Event("GET_TEACHER_DISCIPLINES", disciplines);
        }

        return new Event("BAD_REQUEST", null);
    }



    @MessageExceptionHandler
    public void handleException(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }
}
