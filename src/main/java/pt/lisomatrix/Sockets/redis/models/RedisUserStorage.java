package pt.lisomatrix.Sockets.redis.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import pt.lisomatrix.Sockets.models.*;
import pt.lisomatrix.Sockets.models.Class;

import javax.persistence.Entity;
import java.util.Date;
import java.util.List;

@RedisHash("UserStorage")
public class RedisUserStorage {

    @Id
    private String sessionId;

    private User user;

    private Long userId;

    private String role;

    private Student student;

    private Teacher teacher;

    private Parent parent;

    private Class userClass;

    private List<String> files;

    private List<Absence> absences;

    private List<Lesson> lessons;

    private List<Grade> grades;

    private List<Class> classes;

    private List<Discipline> teacherDisciplines;

    private Class teacherClass;

    private Boolean disconnected;

    private String date;

    private UserSettings userSettings;

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    public Class getTeacherClass() {
        return teacherClass;
    }

    public void setTeacherClass(Class teacherClass) {
        this.teacherClass = teacherClass;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getDisconnected() {
        return disconnected;
    }

    public void setDisconnected(Boolean disconnected) {
        this.disconnected = disconnected;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Discipline> getTeacherDisciplines() {
        return teacherDisciplines;
    }

    public void setTeacherDisciplines(List<Discipline> teacherDisciplines) {
        this.teacherDisciplines = teacherDisciplines;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public Class getUserClass() {
        return userClass;
    }

    public void setUserClass(Class userClass) {
        this.userClass = userClass;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRole() {
        return role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public List<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(List<Absence> absences) {
        this.absences = absences;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }
}
