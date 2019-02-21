package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.Absence;
import pt.lisomatrix.Sockets.models.HourRecuperation;
import pt.lisomatrix.Sockets.repositories.AbsencesRepository;
import pt.lisomatrix.Sockets.repositories.HoursRecuperationRepository;
import pt.lisomatrix.Sockets.requests.models.RecuperateAbsences;
import pt.lisomatrix.Sockets.websocket.models.AbsenceDAO;
import pt.lisomatrix.Sockets.websocket.models.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class HoursRecuperationRestController {

    @Autowired
    private HoursRecuperationRepository hoursRecuperationRepository;

    @Autowired
    private AbsencesRepository absencesRepository;

    @PostMapping("/recuperation/absence")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<AbsenceDAO> recuperateHours(@RequestBody RecuperateAbsences recuperateAbsences) {

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

            return absenceDAOS;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Absences not found");
    }
}
