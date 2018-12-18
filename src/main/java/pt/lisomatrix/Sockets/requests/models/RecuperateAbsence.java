package pt.lisomatrix.Sockets.requests.models;

import pt.lisomatrix.Sockets.websocket.models.AbsenceDAO;

import java.util.List;

public class RecuperateAbsence {

    private List<AbsenceDAO> absenceDAOS;

    public List<AbsenceDAO> getAbsenceDAOS() {
        return absenceDAOS;
    }

    public void setAbsenceDAOS(List<AbsenceDAO> absenceDAOS) {
        this.absenceDAOS = absenceDAOS;
    }
}
