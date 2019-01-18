package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Grade;
import pt.lisomatrix.Sockets.models.ModuleGrade;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.repositories.GradesRepository;
import pt.lisomatrix.Sockets.repositories.ModuleGradesRepository;
import pt.lisomatrix.Sockets.util.SessionHandler;
import pt.lisomatrix.Sockets.websocket.models.Event;
import pt.lisomatrix.Sockets.websocket.models.GradeDAO;
import pt.lisomatrix.Sockets.websocket.models.ModuleGradeDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class GradesController {

    @Autowired
    private GradesRepository gradesRepository;

    @Autowired
    private ModuleGradesRepository moduleGradesRepository;

    @Autowired
    private SessionHandler sessionHandler;

    @MessageMapping("/modules/grades")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Event getModuleGrades(StompHeaderAccessor accessor) {

        Class aClass = (Class) sessionHandler.getAttribute(accessor.getSessionId(), "class");

        Optional<List<ModuleGrade>> foundModuleGrades = moduleGradesRepository.findAllByClass(aClass.getClassId());

        if(foundModuleGrades.isPresent()) {

            List<ModuleGradeDAO> moduleGradeDAOS = populateModuleGradeDAOList(foundModuleGrades.get());

            return new Event("GET_MODULE_GRADES", moduleGradeDAOS);
        }

        return new Event("BAD_REQUEST", null);
    }

    @MessageMapping("/class/grades")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasRole('ROLE_ALUNO')")
    public Event getClassGrades(StompHeaderAccessor accessor) {

        String role = (String) sessionHandler.getAttribute(accessor.getSessionId(), "role");

        Optional<List<Grade>> foundGrades = Optional.empty();

        if(role.equals(Roles.PROFESSOR.toString())) {

            Class aClass = (Class) sessionHandler.getAttribute(accessor.getSessionId(), "class");

            Long[] studentIds = new Long[aClass.getStudents().size()];

            for(int i = 0; i < aClass.getStudents().size(); i++) {
                studentIds[i] = aClass.getStudents().get(i).getStudentId();
            }

            foundGrades = gradesRepository.findAllByStudentId(studentIds);

        } else if(role.equals(Roles.ALUNO.toString())) {

            Student student = (Student) sessionHandler.getAttribute(accessor.getSessionId(), "student");

            foundGrades = gradesRepository.findAllByStudentIsIn(student);
        }

        if(foundGrades.isPresent()) {

            List<Grade> grades = foundGrades.get();

            List<GradeDAO> gradeDAOS = populateDAOList(grades);

            return new Event("GET_GRADES", gradeDAOS);
        }

        return new Event("BAD_REQUEST", null);
    }

    private List<GradeDAO> populateDAOList(List<Grade> grades) {

        List<GradeDAO> gradeDAOS = new ArrayList<>();

        for(int i = 0; i < grades.size(); i++) {

            GradeDAO gradeDAO = new GradeDAO();

            gradeDAO.populate(grades.get(i));

            gradeDAOS.add(gradeDAO);
        }

        return gradeDAOS;
    }

    private List<ModuleGradeDAO> populateModuleGradeDAOList(List<ModuleGrade> moduleGrades) {

        List<ModuleGradeDAO> moduleGradeDAOS = new ArrayList<>();

        for(int i = 0; i < moduleGrades.size(); i++) {

            ModuleGrade moduleGrade = moduleGrades.get(i);

            ModuleGradeDAO moduleGradeDAO = new ModuleGradeDAO();

            moduleGradeDAO.setModuleGrade(moduleGrade.getGrade());
            moduleGradeDAO.setModuleGradeId(moduleGrade.getModuleGradeId());
            moduleGradeDAO.setModuleId(moduleGrade.getModule().getModuleId());
            moduleGradeDAO.setStudentId(moduleGrade.getStudent().getStudentId());

            moduleGradeDAOS.add(moduleGradeDAO);
        }

        return moduleGradeDAOS;
    }

}
