package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.ModuleGrade;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.ModuleGradesRepository;
import pt.lisomatrix.Sockets.repositories.StudentsRepository;
import pt.lisomatrix.Sockets.websocket.models.ModuleGradeDAO;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class GradesRestController {

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private ModuleGradesRepository moduleGradesRepository;

    @GetMapping("/class/module/grade")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<ModuleGradeDAO> getClassModuleGrades(Principal principal) throws Exception {

        Class aClass = classesRepository.findFirstByClassDirectorUserId(Long.parseLong(principal.getName())).get();

        Optional<List<ModuleGrade>> foundModuleGrades = moduleGradesRepository.findAllByClass(aClass.getClassId());

        if(foundModuleGrades.isPresent()) {

            List<ModuleGradeDAO> moduleGradeDAOS = populateModuleGradeDAOList(foundModuleGrades.get());

            return moduleGradeDAOS;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Module grades not found");
    }

    @GetMapping("/student/module/grades")
    @PreAuthorize("hasRole('ROLE_ALUNO')")
    @CrossOrigin
    public List<ModuleGradeDAO> getStudentModuleGrades(Principal principal) throws Exception {


        Student student = studentsRepository.findFirstByUsername(principal.getName()).get();

        Optional<List<ModuleGrade>> foundModuleGrades = moduleGradesRepository.findAllByStudent(student);

        if(foundModuleGrades.isPresent()) {

            List<ModuleGradeDAO> moduleGradeDAOS = populateModuleGradeDAOList(foundModuleGrades.get());

            return moduleGradeDAOS;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Module grades not found");
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
