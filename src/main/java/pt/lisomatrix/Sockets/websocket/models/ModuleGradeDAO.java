package pt.lisomatrix.Sockets.websocket.models;

public class ModuleGradeDAO {

    private long moduleGradeId;

    private long moduleGrade;

    private long moduleId;

    private Long studentId;

    public long getModuleGradeId() {
        return moduleGradeId;
    }

    public void setModuleGradeId(long moduleGradeId) {
        this.moduleGradeId = moduleGradeId;
    }

    public long getModuleGrade() {
        return moduleGrade;
    }

    public void setModuleGrade(long moduleGrade) {
        this.moduleGrade = moduleGrade;
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
