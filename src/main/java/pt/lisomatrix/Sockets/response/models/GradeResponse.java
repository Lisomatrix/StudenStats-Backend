package pt.lisomatrix.Sockets.response.models;

import pt.lisomatrix.Sockets.models.Grade;

public class GradeResponse {

    private long gradeId;

    private Long studentId;

    private long disciplineId;

    private Long teacherId;

    private long moduleId;

    private long grade;

    public void populate(Grade grade) {

        setDisciplineId(grade.getDiscipline().getDisciplineId());
        setGrade(grade.getGrade());
        setGradeId(grade.getGradeId());
        setModuleId(grade.getModule().getModuleId());
        setStudentId(grade.getStudent().getStudentId());
        setTeacherId(grade.getTeacher().getTeacherId());
    }

    public long getGradeId() {
        return gradeId;
    }

    public void setGradeId(long gradeId) {
        this.gradeId = gradeId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public long getGrade() {
        return grade;
    }

    public void setGrade(long grade) {
        this.grade = grade;
    }
}
