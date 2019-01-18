package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import pt.lisomatrix.Sockets.models.Absence;
import pt.lisomatrix.Sockets.models.HourRecuperation;
import pt.lisomatrix.Sockets.repositories.AbsencesRepository;
import pt.lisomatrix.Sockets.repositories.HoursRecuperationRepository;
import pt.lisomatrix.Sockets.requests.models.RecuperateAbsences;
import pt.lisomatrix.Sockets.util.SessionHandler;
import pt.lisomatrix.Sockets.websocket.models.AbsenceDAO;
import pt.lisomatrix.Sockets.websocket.models.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class HoursRecuperationController {

    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private HoursRecuperationRepository hoursRecuperationRepository;

    @Autowired
    private AbsencesRepository absencesRepository;

    @MessageMapping("/hours/recuperate")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Event recuperateHours(RecuperateAbsences recuperateAbsences) {

        List<Long> ids = new ArrayList<>();

        for (int i = 0; i < recuperateAbsences.getAbsences().size(); i++) {
            ids.add(recuperateAbsences.getAbsences().get(i).getAbsenceId());
        }

        List<Absence> foundAbsences = absencesRepository.findAllById(ids);

        if (foundAbsences != null && foundAbsences.size() > 0) {

            HourRecuperation hourRecuperation = new HourRecuperation();

            hourRecuperation.setAbsences(foundAbsences);
            hourRecuperation.setDate(new Date());

            hoursRecuperationRepository.save(hourRecuperation);

            List<AbsenceDAO> absenceDAOS = new ArrayList<>();

            for (int i = 0; i < foundAbsences.size(); i++) {

                AbsenceDAO absenceDAO = new AbsenceDAO();
                Absence absence = foundAbsences.get(i);

                absence.setRecuperated(true);

                absenceDAO.populate(absence);
                absenceDAOS.add(absenceDAO);
            }

            absencesRepository.saveAll(foundAbsences);

            return new Event("HOUR_RECUPERATED", absenceDAOS);
        }

        return new Event("BAD_REQUEST", null);
    }
}
