package pt.lisomatrix.Sockets.requests.models;

import pt.lisomatrix.Sockets.websocket.models.AbsenceDAO;

import java.util.List;

public class RecuperateAbsences {

    private List<AbsenceDAO> absences;

    public List<AbsenceDAO> getAbsences() {
        return absences;
    }

    public void setAbsences(List<AbsenceDAO> absences) {
        this.absences = absences;
    }
}
