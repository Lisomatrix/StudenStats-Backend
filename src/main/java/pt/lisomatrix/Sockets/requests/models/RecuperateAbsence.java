package pt.lisomatrix.Sockets.requests.models;

import pt.lisomatrix.Sockets.websocket.models.AbsenceDAO;

public class RecuperateAbsence {

    private AbsenceDAO absence;

    public AbsenceDAO getAbsence() {
        return absence;
    }

    public void setAbsence(AbsenceDAO absence) {
        this.absence = absence;
    }
}
