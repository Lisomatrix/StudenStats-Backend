package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.*;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.repositories.*;
import pt.lisomatrix.Sockets.util.SessionHandler;
import pt.lisomatrix.Sockets.websocket.models.Event;
import pt.lisomatrix.Sockets.websocket.models.ScheduleDAO;

import java.util.List;
import java.util.Optional;

@Controller
public class SchedulesController {

    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private ScheduleExceptionsRepository scheduleExceptionsRepository;

    @Autowired
    private SchedulesRepository schedulesRepository;

    @Autowired
    private ScheduleHoursRepository scheduleHoursRepository;

    @Autowired
    private HoursRepository hoursRepository;

    @Autowired
    private ScheduleDayRepository scheduleDayRepository;

    @MessageMapping("/hours")
    @SendToUser("/queue/reply")
    public Event getHours() {

        List<Hour> hours = hoursRepository.findAll();

        return new Event("GET_HOURS", hours);
    }

    @MessageMapping("/days")
    @SendToUser("/queue/reply")
    public Event getDays() {

        List<ScheduleDay> scheduleDays = scheduleDayRepository.findAll();

        return new Event("GET_DAYS", scheduleDays);
    }

    @MessageMapping("/schedule")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasRole('ROLE_ALUNO')")
    public Event getSchedule(StompHeaderAccessor accessor) {

        String role = (String) sessionHandler.getAttribute(accessor.getSessionId(), "role");

        Optional<Schedule> foundSchedule = null;

        if(role.equals(Roles.PROFESSOR.toString())) {

            Teacher teacher = (Teacher) sessionHandler.getAttribute(accessor.getSessionId(), "teacher");

            foundSchedule = schedulesRepository.findFirstByTeacher(teacher.getTeacherId());

        } else if(role.equals(Roles.ALUNO.toString())) {

            Class aClass = (Class) sessionHandler.getAttribute(accessor.getSessionId(), "class");

            foundSchedule = schedulesRepository.findFirstByClass(aClass.getClassId());
        }

        if(foundSchedule != null) {

            Schedule schedule = foundSchedule.get();

            ScheduleDAO scheduleDAO = new ScheduleDAO();

            scheduleDAO.populate(schedule);

            return new Event("GET_SCHEDULE", scheduleDAO);
        }

        return new Event("BAD_REQUEST", null);
    }
}
