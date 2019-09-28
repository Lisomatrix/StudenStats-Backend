package pt.lisomatrix.Sockets.requests.models;

import pt.lisomatrix.Sockets.response.models.AbsenceResponse;

import java.util.List;

public class RecuperateAbsences {

    private List<AbsenceResponse> absences;

    public List<AbsenceResponse> getAbsences() {
        return absences;
    }

    public void setAbsences(List<AbsenceResponse> absences) {
        this.absences = absences;
    }
}
