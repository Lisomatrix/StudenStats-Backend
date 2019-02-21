package pt.lisomatrix.Sockets.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.StudentsRepository;
import pt.lisomatrix.Sockets.repositories.TeachersRepository;
import pt.lisomatrix.Sockets.websocket.models.StudentDAO;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class StudentsRestController {

    private ClassesRepository classesRepository;

    private StudentsRepository studentsRepository;

    private TeachersRepository teachersRepository;

    public StudentsRestController(ClassesRepository classesRepository, StudentsRepository studentsRepository, TeachersRepository teachersRepository) {
        this.classesRepository = classesRepository;
        this.studentsRepository = studentsRepository;
        this.teachersRepository = teachersRepository;
    }

    @GetMapping("/class/{classId}/student")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<StudentDAO> getStudents(@PathVariable("classId") long classId, Principal principal) {

        Teacher teacher = teachersRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

        Optional<Class> foundClass = classesRepository.findById(classId);

        if(foundClass.isPresent()) {

            Class aClass = foundClass.get();


            Boolean isAllowed = false;

            for(int i = 0; i < aClass.getTeachers().size(); i++) {
                if(aClass.getTeachers().get(i).getTeacherId().equals(teacher.getTeacherId())) {
                    isAllowed = true;
                    break;
                }
            }

            if(isAllowed) {

                List<Student> students = aClass.getStudents();

                List<StudentDAO> studentDAOList = populateStudentDAOList(students, aClass.getClassId());

                return studentDAOList;

            } else {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Class not found");
    }

    /***
     * Helper to convert List<Student> to List<StudentDAO>
     *
     * @param students
     * @param classId
     * @return
     */
    private List<StudentDAO> populateStudentDAOList(List<Student> students, long classId) {

        List<StudentDAO> studentDAOList = new ArrayList<StudentDAO>();

        for(int i = 0; i < students.size(); i++) {

            StudentDAO studentDAO = new StudentDAO();

            studentDAO.populate(students.get(i), classId);

            studentDAOList.add(studentDAO);
        }

        return studentDAOList;

    }
}
