package pt.lisomatrix.Sockets.requests.models;

public class UpdateModuleGrade {

    private int moduleGrade;

    private long studentId;

    public int getModuleGrade() {
        return moduleGrade;
    }

    public void setModuleGrade(int moduleGrade) {
        this.moduleGrade = moduleGrade;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }
}
