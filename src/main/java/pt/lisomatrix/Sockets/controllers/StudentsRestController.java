package pt.lisomatrix.Sockets.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Parent;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.ParentsRepository;
import pt.lisomatrix.Sockets.repositories.StudentsRepository;
import pt.lisomatrix.Sockets.repositories.TeachersRepository;
import pt.lisomatrix.Sockets.response.models.StudentResponse;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class StudentsRestController {

    private ClassesRepository classesRepository;

    private StudentsRepository studentsRepository;

    private TeachersRepository teachersRepository;

    private ParentsRepository parentsRepository;

    public StudentsRestController(ClassesRepository classesRepository, StudentsRepository studentsRepository, TeachersRepository teachersRepository, ParentsRepository parentsRepository) {
        this.classesRepository = classesRepository;
        this.studentsRepository = studentsRepository;
        this.teachersRepository = teachersRepository;
        this.parentsRepository = parentsRepository;
    }

    @PostMapping("/parent/{parentUserId}/student/{studentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ResponseEntity<?> addParentChild(@PathVariable long parentUserId, @PathVariable long studentId) {

        Optional<Student> foundStudent = studentsRepository.findById(studentId);

        if(!foundStudent.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Student not found!");
        }

        Student student = foundStudent.get();

        Optional<Parent> foundParent = parentsRepository.findFirstByUserId(parentUserId);

        if(foundParent.isPresent()) {

            Parent parent = foundParent.get();

            List<Student> children = parent.getChildren();
            children.add(student);

            parent.setChildren(children);

            parentsRepository.save(parent);

            StudentResponse studentResponse = new StudentResponse();

            studentResponse.populate(student);

            return ResponseEntity.ok(studentResponse);
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Parent not found!");
    }

    @DeleteMapping("/parent/{parentUserId}/student/{studentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ResponseEntity<?> removeParentChild(@PathVariable long parentUserId, @PathVariable long studentId) {

        Optional<Student> foundStudent = studentsRepository.findById(studentId);

        if(!foundStudent.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Student not found!");
        }

        Student student = foundStudent.get();

        Optional<Parent> foundParent = parentsRepository.findFirstByUserId(parentUserId);

        if(foundParent.isPresent()) {
            Parent parent = foundParent.get();

            parent.getChildren().remove(student);

            parentsRepository.save(parent);

            return ResponseEntity.ok().build();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Parent not found!");
    }

    @GetMapping("/class/{classId}/student")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public List<StudentResponse> getStudents(@PathVariable("classId") long classId, Principal principal) {

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

                List<StudentResponse> studentResponseList = populateStudentDAOList(students, aClass.getClassId());

                return studentResponseList;

            } else {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Class not found");
    }

    @GetMapping("/parent/student")
    @PreAuthorize("hasRole('ROLE_PARENT')")
    @CrossOrigin
    public List<StudentResponse> getParentStudents(Principal principal) {

        Optional<List<Student>> foundStudents = studentsRepository.findAllByParentUserId(Long.parseLong(principal.getName()));

        if(foundStudents.isPresent()) {

            return populateStudentDAOList(foundStudents.get());
        }

        return new ArrayList<>();
    }

    @GetMapping("/parent/{parentUserId}/student")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public List<StudentResponse> getParentChildren(@PathVariable long parentUserId) {

        Optional<Parent> foundParent = parentsRepository.findFirstByUserId(parentUserId);

        if(foundParent.isPresent()) {
            Optional<List<Student>> foundStudents = studentsRepository.findAllByParentUserId(parentUserId);

            if(foundStudents.isPresent()) {

                return populateStudentDAOList(foundStudents.get());
            }

            return new ArrayList<>();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Parent not found");
    }

    @GetMapping("/admin/student/without/parent")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public List<StudentResponse> getStudentsWithoutParent() {
        return populateStudentDAOList(studentsRepository.findAllWithoutParent());
    }

    @GetMapping("/user/{userId}/student")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ResponseEntity<?> getStudent(@PathVariable long userId) {

        Optional<Student> foundStudent = studentsRepository.findFirstByUserId(userId);

        if(foundStudent.isPresent()) {

            StudentResponse studentResponse = new StudentResponse();
            studentResponse.populate(foundStudent.get());

            Optional<Class> foundClass = classesRepository.findFirstByStudentsIn(foundStudent.get());

            if(foundClass.isPresent()) {
                studentResponse.setClassId(foundClass.get().getClassId());
            }

            return ResponseEntity.ok(studentResponse);
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Student not found");
    }

    private List<StudentResponse> populateStudentDAOList(List<Student> students) {

        List<StudentResponse> studentResponseList = new ArrayList<StudentResponse>();

        for(int i = 0; i < students.size(); i++) {

            StudentResponse studentResponse = new StudentResponse();

            studentResponse.populate(students.get(i));

            studentResponseList.add(studentResponse);
        }

        return studentResponseList;

    }


    private List<StudentResponse> populateStudentDAOList(List<Student> students, long classId) {

        List<StudentResponse> studentResponseList = new ArrayList<StudentResponse>();

        for(int i = 0; i < students.size(); i++) {

            StudentResponse studentResponse = new StudentResponse();

            studentResponse.populate(students.get(i), classId);

            studentResponseList.add(studentResponse);
        }

        return studentResponseList;

    }
}
