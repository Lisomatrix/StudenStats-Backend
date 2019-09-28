package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.*;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.repositories.*;
import pt.lisomatrix.Sockets.response.models.AccountResponse;
import pt.lisomatrix.Sockets.response.models.StudentResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class AdminRestController {

    @Autowired
    private TeachersRepository teachersRepository;

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private ParentsRepository parentsRepository;

    @Autowired
    private AdminsRepository adminsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @GetMapping("/admin/teacher")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public List<Teacher> getTeachers() {
        return teachersRepository.findAll();
    }

    @GetMapping("/admin/teacher/free")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public List<Teacher> getNonClassDirectors() {
        return teachersRepository.findAllWithoutClass();
    }

    @GetMapping("/admin/teacher/director")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public List<Teacher> getClassDirectors() {
        return teachersRepository.findAllByWithClass();
    }

    @GetMapping("/admin/account")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public List<AccountResponse> getAccounts() {

        List<User> allUsers = usersRepository.findAllActiveUsers();

        List<AccountResponse> accountResponses = new ArrayList<>();

        for(int i = 0; i < allUsers.size(); i++) {
            User temp = allUsers.get(i);

            if(temp.getRole().getRole().equals(Roles.ALUNO.toString())) {
                AccountResponse accountResponse = getStudentAccountData(temp);

                if(accountResponse != null) {
                    accountResponses.add(accountResponse);
                }
            } else if(temp.getRole().getRole().equals(Roles.PARENTE.toString())) {
                AccountResponse accountResponse = getParentAccountData(temp);

                if(accountResponse != null) {
                    accountResponses.add(accountResponse);
                }
            } else if(temp.getRole().getRole().equals(Roles.PROFESSOR.toString())) {
                AccountResponse accountResponse = getTeacherAccountData(temp);

                if(accountResponse != null) {
                    accountResponses.add(accountResponse);
                }
            } else if(temp.getRole().getRole().equals(Roles.ADMIN.toString())) {
                AccountResponse accountResponse = getAdminAccountData(temp);

                if(accountResponse != null) {
                    accountResponses.add(accountResponse);
                }
            }
        }

        return accountResponses;
    }

    @GetMapping("/admin/account/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public AccountResponse getAccount(@PathVariable long userId) {
        Optional<User> foundUser = usersRepository.findById(userId);

        if(foundUser.isPresent()) {
            User user = foundUser.get();

            if(user.getRole().getRole().equals(Roles.ALUNO.toString())) {
                return getStudentAccountData(user);
            } else if(user.getRole().getRole().equals(Roles.PARENTE.toString())) {
                return getParentAccountData(user);
            } else if(user.getRole().getRole().equals(Roles.PROFESSOR.toString())) {
                return getTeacherAccountData(user);

            } else if(user.getRole().getRole().equals(Roles.ADMIN.toString())) {
                return getAdminAccountData(user);
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found");
    }

    @GetMapping("/admin/student")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public List<StudentResponse> getStudents() {

        List<StudentResponse> students = new ArrayList<>();

        List<Class> classes = classesRepository.findAll();

        for(int i = 0; i < classes.size(); i++) {
            students.addAll(populateStudentDAOList(classes.get(i).getStudents(), classes.get(i).getClassId()));
        }

        Optional<List<Student>> foundStudentsWithoutClass = studentsRepository.findAllStudentsWithoutClass();

        if(foundStudentsWithoutClass.isPresent()) {
            students.addAll(populateStudentDAOList(foundStudentsWithoutClass.get(), -1l));
        }

        return students;
    }

    private AccountResponse getAdminAccountData(User user) {

        Optional<Admin> foundAdmin = adminsRepository.findFirstByUser(user);

        if(foundAdmin.isPresent()) {
            return new AccountResponse(user, foundAdmin.get());
        } else {
            return null;
        }
    }

    private AccountResponse getTeacherAccountData(User user) {

        Optional<Teacher> foundTeacher = teachersRepository.findFirstByUser(user);

        if(foundTeacher.isPresent()) {
            return new AccountResponse(user, foundTeacher.get());
        } else {
            return null;
        }
    }

    private AccountResponse getParentAccountData(User user) {

        Optional<Parent> foundParent = parentsRepository.findFirstByUser(user);

        if(foundParent.isPresent()) {
            return new AccountResponse(user, foundParent.get());
        } else {
            return null;
        }
    }

    private AccountResponse getStudentAccountData(User user) {

        Optional<Student> foundStudent = studentsRepository.findFirstByUser(user);

        if(foundStudent.isPresent()) {
            return new AccountResponse(user, foundStudent.get());
        } else {
            return null;
        }
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
