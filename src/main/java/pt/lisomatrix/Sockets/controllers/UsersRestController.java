package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.*;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.repositories.*;
import pt.lisomatrix.Sockets.requests.models.NewUser;
import pt.lisomatrix.Sockets.requests.models.UpdateUser;
import pt.lisomatrix.Sockets.response.models.ParentResponse;
import pt.lisomatrix.Sockets.response.models.ThemeResponse;
import pt.lisomatrix.Sockets.response.models.UserResponse;
import pt.lisomatrix.Sockets.util.RandomCodeGenerator;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UsersRestController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    @Autowired
    private TeachersRepository teachersRepository;

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private AdminsRepository adminsRepository;

    @Autowired
    private ParentsRepository parentsRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RandomCodeGenerator randomCodeGenerator;

    private final String defaultTheme = "{\"dark\":true,\"primaryColor\":\"#eb3030e1\",\"secondaryColor\":\"#1890ff\",\"textPrimaryColor\":\"#000\",\"textSecondaryColor\":\"#000\",\"headerColor\":\"#dddddd\",\"backgroundColor\":\"#dddddd\",\"cardBackground\":\"#ffffff\"}";

    private List<Role> roles;

    @PostConstruct
    public void getConstantData() {
        roles = roleRepository.findAll();
    }

    @GetMapping("/user")
    @CrossOrigin
    public UserResponse getUser(Principal principal) throws Exception {

        Optional<User> foundUser = usersRepository.findById(Long.parseLong(principal.getName()));

        if(foundUser.isPresent()) {

            User user = foundUser.get();

            String role = user.getRole().getRole();

            UserResponse userResponse = new UserResponse();

            userResponse.setClassDirector(null);
            userResponse.setUserId(user.getUserId());
            userResponse.setEmail(user.getUsername());
            userResponse.setRole(role);

            Optional<UserSettings> foundUserSettings = userSettingsRepository.findFirstByUser(user);

            if(foundUserSettings.isPresent()) {
                UserSettings userSettings = foundUserSettings.get();

                userResponse.setUserThemeSettings(userSettings.getTheme());
            }

            if(role.equals(Roles.PROFESSOR.toString())) {

                Optional<Teacher> foundTeacher = teachersRepository.findFirstByUser(user);

                Teacher teacher = foundTeacher.get();

                Optional<Class> foundClass = classesRepository.findFirstByClassDirector(teacher);

                if(foundClass.isPresent()) {
                    userResponse.setClassDirector(true);
                }

                userResponse.setRoleEntityId(teacher.getTeacherId());
                userResponse.setId(teacher.getTeacherId());
                userResponse.setName(teacher.getName());
                userResponse.setAddress(teacher.getAddress());
                userResponse.setPhoneNumber(teacher.getMobilePhone());


            } else if(role.equals(Roles.ALUNO.toString())) {

                Student student = studentsRepository.findFirstByUser(user).get();

                userResponse.setId(student.getStudentId());
                userResponse.setName(student.getName());
                userResponse.setRoleEntityId(student.getStudentId());
                userResponse.setAddress(student.getAddress());
                userResponse.setPhoneNumber(student.getMobilePhone());

            } else if(role.equals(Roles.PARENTE.toString())) {

                Parent parent = parentsRepository.findFirstByUser(user).get();

                userResponse.setId(parent.getParentId());
                userResponse.setName(parent.getName());
                userResponse.setRoleEntityId(parent.getParentId());
                userResponse.setAddress(parent.getAddress());
                userResponse.setPhoneNumber(parent.getMobilePhone());

            } else if(role.equals(Roles.ADMIN.toString())) {

                Admin admin = adminsRepository.findFirstByUser(user).get();

                userResponse.setId(admin.getAdminId());
                userResponse.setName(admin.getName());
                userResponse.setRoleEntityId(admin.getAdminId());
                userResponse.setAddress(admin.getAddress());
                userResponse.setPhoneNumber(admin.getMobilePhone());
            }

            return userResponse;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found");
    }

    @PostMapping("/user/theme")
    @CrossOrigin
    public ThemeResponse updateTheme(@RequestBody ThemeResponse theme, Principal principal) {

        Optional<User> foundUser = usersRepository.findById(Long.parseLong(principal.getName()));

        if(foundUser.isPresent()) {

            User user = foundUser.get();

            Optional<UserSettings> foundUserSettings = userSettingsRepository.findFirstByUser(user);

            ThemeResponse currentTheme;

            UserSettings userSettings;

            if(foundUserSettings.isPresent()) {

                userSettings = foundUserSettings.get();

                currentTheme = new ThemeResponse();

                currentTheme.populate(userSettings.getTheme());

            } else {
                userSettings = new UserSettings();

                userSettings.setUser(user);
                currentTheme = new ThemeResponse();
            }

            if(validateColor(theme.getBackgroundColor())) {
                currentTheme.setBackgroundColor(theme.getBackgroundColor());
            }

            if(validateColor(theme.getHeaderColor())) {
                currentTheme.setHeaderColor(theme.getHeaderColor());
            }

            if(validateColor(theme.getPrimaryColor())) {
                currentTheme.setPrimaryColor(theme.getPrimaryColor());
            }

            if(validateColor(theme.getSecondaryColor())) {
                currentTheme.setSecondaryColor(theme.getSecondaryColor());
            }

            if(validateColor(theme.getTextPrimaryColor())) {
                currentTheme.setTextPrimaryColor(theme.getTextPrimaryColor());
            }

            if(validateColor(theme.getTextSecondaryColor())) {
                currentTheme.setTextSecondaryColor(theme.getTextSecondaryColor());
            }

            if(validateColor(theme.getCardBackground())) {
                currentTheme.setCardBackground(theme.getCardBackground());
            }

            if(validateColor(theme.getIconColor())) {
                currentTheme.setIconColor(theme.getIconColor());
            }

            if(validateColor(theme.getButtonColor())) {
                currentTheme.setButtonColor(theme.getButtonColor());
            }

            currentTheme.setDark(theme.getDark());

            userSettings.setTheme(currentTheme.toJSON());

            userSettingsRepository.save(userSettings);

            return currentTheme;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found"
            );
        }
    }

    @GetMapping("/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public List<Role> getRoles() {

        if(roles == null) {
            getConstantData();
        }

        return roles;
    }

    @GetMapping("/student/{studentId}/parent")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ParentResponse getStudentParent(@PathVariable long studentId) {
        Optional<Student> foundStudent = studentsRepository.findById(studentId);

        if(foundStudent.isPresent()) {

            Optional<Parent> foundParent = parentsRepository.findFirstByStudentId(studentId);

            if(foundParent.isPresent()) {

                Parent parent = foundParent.get();

                ParentResponse parentResponse = new ParentResponse();

                parentResponse.setName(parent.getName());
                parentResponse.setParentId(parent.getParentId());
                parentResponse.setUserId(parent.getUser().getUserId());

                return parentResponse;
            }

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent not found");
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
    }

    @PostMapping("/user/student")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public UserResponse addStudent(@RequestBody NewUser newUser) {

        User user = populateUser(newUser);

        Student student = populateStudent(newUser, user);

        User savedUser = usersRepository.save(user);

        setUserSettings(savedUser);

        Student savedStudent = studentsRepository.save(student);

        UserResponse userResponse = new UserResponse();

        userResponse.setClassDirector(false);
        userResponse.setId(savedStudent.getStudentId());
        userResponse.setName(student.getName());
        userResponse.setRoleEntityId(savedStudent.getStudentId());
        userResponse.setUserId(user.getUserId());
        userResponse.setUserThemeSettings(defaultTheme);

        return userResponse;
    }

    @PostMapping("/user/teacher")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public UserResponse addTeacher(@RequestBody NewUser newUser) {

        User user = populateUser(newUser);

        Teacher teacher = populateTeacher(newUser, user);

        User savedUser = usersRepository.save(user);

        setUserSettings(savedUser);

        Teacher savedTeacher = teachersRepository.save(teacher);

        UserResponse userResponse = new UserResponse();

        userResponse.setClassDirector(false);
        userResponse.setId(savedTeacher.getTeacherId());
        userResponse.setName(savedTeacher.getName());
        userResponse.setRoleEntityId(savedTeacher.getTeacherId());
        userResponse.setUserId(user.getUserId());
        userResponse.setUserThemeSettings(defaultTheme);

        return userResponse;
    }

    @PostMapping("/user/parent")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public UserResponse addParent(@RequestBody NewUser newUser) {

        User user = populateUser(newUser);

        Parent parent = populateParent(newUser, user);

        User savedUser = usersRepository.save(user);

        setUserSettings(savedUser);

        Parent savedParent = parentsRepository.save(parent);

        UserResponse userResponse = new UserResponse();

        userResponse.setClassDirector(false);
        userResponse.setId(savedParent.getParentId());
        userResponse.setName(savedParent.getName());
        userResponse.setRoleEntityId(savedParent.getParentId());
        userResponse.setUserId(user.getUserId());
        userResponse.setUserThemeSettings(defaultTheme);

        return userResponse;
    }

    @PostMapping("/user/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public UserResponse addAdmin(@RequestBody NewUser newUser) {

        User user = populateUser(newUser);

        Admin admin = populateAdmin(newUser, user);

        User savedUser = usersRepository.save(user);

        setUserSettings(savedUser);

        Admin savedAdmin = adminsRepository.save(admin);

        UserResponse userResponse = new UserResponse();

        userResponse.setClassDirector(false);
        userResponse.setId(user.getUserId());
        userResponse.setName(savedAdmin.getName());
        userResponse.setRoleEntityId(savedAdmin.getAdminId());
        userResponse.setUserId(user.getUserId());
        userResponse.setUserThemeSettings(defaultTheme);

        return userResponse;
    }

    @PutMapping("/user/{userId}/student/")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ALUNO')")
    @CrossOrigin
    public UserResponse updateStudent(@RequestBody NewUser newUser, @PathVariable long userId) {

        User user = populateUser(newUser);

        user.setUserId(userId);

        Student student = populateStudent(newUser, user);

        User savedUser = usersRepository.save(user);

        Student savedStudent = studentsRepository.save(student);

        UserResponse userResponse = new UserResponse();

        userResponse.setClassDirector(false);
        userResponse.setId(savedStudent.getStudentId());
        userResponse.setName(student.getName());
        userResponse.setRoleEntityId(savedStudent.getStudentId());
        userResponse.setUserId(savedUser.getUserId());
        userResponse.setUserThemeSettings(defaultTheme);

        return userResponse;
    }

    @PutMapping("/user/{userId}/teacher")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public UserResponse updateTeacher(@RequestBody UpdateUser updateUser, @PathVariable long userId) {

        Optional<User> foundUser = usersRepository.findById(userId);

        if(!foundUser.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found");
        }

        User user = foundUser.get();

        user.setUsername(updateUser.getEmail());

        Optional<Teacher> foundTeacher = teachersRepository.findFirstByUser(user);

        if(foundTeacher.isPresent()) {

            User savedUser = usersRepository.save(user);

            Teacher teacher = foundTeacher.get();

            teacher.setName(updateUser.getName());
            teacher.setAddress(updateUser.getAddress());
            teacher.setMobilePhone(updateUser.getPhoneNumber());

            Teacher savedTeacher = teachersRepository.save(teacher);

            UserResponse userResponse = new UserResponse();

            userResponse.setClassDirector(false);
            userResponse.setId(savedTeacher.getTeacherId());
            userResponse.setName(teacher.getName());
            userResponse.setRoleEntityId(savedTeacher.getTeacherId());
            userResponse.setUserId(savedUser.getUserId());
            userResponse.setUserThemeSettings(defaultTheme);

            return userResponse;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Teacher not found");
    }

    @PutMapping("/user/{userId}/parent")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PARENT')")
    @CrossOrigin
    public UserResponse updateParent(@RequestBody UpdateUser updateUser, @PathVariable long userId) {

        Optional<User> foundUser = usersRepository.findById(userId);

        if(!foundUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User user = foundUser.get();

        user.setUsername(updateUser.getEmail());

        Optional<Parent> foundParent = parentsRepository.findFirstByUser(user);

        if(foundParent.isPresent()) {

            User savedUser = usersRepository.save(user);

            Parent parent = foundParent.get();

            parent.setAddress(updateUser.getAddress());
            parent.setName(updateUser.getName());
            parent.setMobilePhone(updateUser.getPhoneNumber());

            Parent savedParent = parentsRepository.save(parent);

            UserResponse userResponse = new UserResponse();

            userResponse.setClassDirector(false);
            userResponse.setId(savedParent.getParentId());
            userResponse.setName(savedParent.getName());
            userResponse.setRoleEntityId(parent.getParentId());
            userResponse.setUserId(savedUser.getUserId());
            userResponse.setUserThemeSettings(defaultTheme);

            return userResponse;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent not found");
    }

    @PutMapping("/user/{userId}/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public UserResponse updateAdmin(@RequestBody UpdateUser updateUser, @PathVariable long userId) {

        Optional<User> foundUser = usersRepository.findById(userId);

        if(!foundUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User user = foundUser.get();

        user.setUsername(updateUser.getEmail());

        Optional<Admin> foundAdmin = adminsRepository.findFirstByUser(user);

        if(foundAdmin.isPresent()) {

            User savedUser = usersRepository.save(user);

            Admin admin = foundAdmin.get();

            admin.setAddress(updateUser.getAddress());
            admin.setName(updateUser.getName());
            admin.setMobilePhone(updateUser.getPhoneNumber());

            Admin savedAdmin = adminsRepository.save(admin);

            UserResponse userResponse = new UserResponse();

            userResponse.setClassDirector(false);
            userResponse.setId(savedAdmin.getAdminId());
            userResponse.setName(savedAdmin.getName());
            userResponse.setRoleEntityId(admin.getAdminId());
            userResponse.setUserId(savedUser.getUserId());
            userResponse.setUserThemeSettings(defaultTheme);

            return userResponse;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found");
    }

    @DeleteMapping("/admin/user/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ResponseEntity<?> deleteTeacher(@PathVariable long userId) {

        Optional<User> foundUser = usersRepository.findById(userId);

        if(foundUser.isPresent()) {

            User user = foundUser.get();

            user.setActive(false);
            user.setRegistrationCode(null);

            usersRepository.save(user);

            return ResponseEntity.ok().build();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found");
    }

    private void setUserSettings(User user) {

        UserSettings userSettings = new UserSettings();

        userSettings.setUser(user);
        userSettings.setAllowEmails(true);
        userSettings.setTheme(defaultTheme);

        userSettingsRepository.save(userSettings);
    }

    private Parent populateParent(NewUser newUser, User user) {
        Parent parent = new Parent();

        parent.setAddress(newUser.getAddress());
        parent.setBirthDate(newUser.getBirthDate());
        parent.setMobilePhone(newUser.getPhoneNumber());
        parent.setName(newUser.getName());
        parent.setUser(user);

        return parent;
    }

    private Admin populateAdmin(NewUser newUser, User user) {
        Admin admin = new Admin();

        admin.setAddress(newUser.getAddress());
        admin.setBirthDate(newUser.getBirthDate());
        admin.setMobilePhone(newUser.getPhoneNumber());
        admin.setName(newUser.getName());
        admin.setUser(user);

        return admin;
    }

    private Teacher populateTeacher(NewUser newUser, User user) {
        Teacher teacher = new Teacher();

        teacher.setBirthDate(newUser.getBirthDate());
        teacher.setName(newUser.getName());
        teacher.setUser(user);

        return teacher;
    }

    private Student populateStudent(NewUser newUser, User user) {
        Student student = new Student();

        student.setUser(user);
        student.setAddress(newUser.getAddress());
        student.setBirthDate(newUser.getBirthDate());
        student.setMobilePhone(newUser.getPhoneNumber());
        student.setName(newUser.getName());

        return student;
    }

    private User populateUser(NewUser newUser) {
        User user = new User();

        user.setCreated(false);
        user.setActive(true);
        user.setPassword("");

        if(roles == null) {
            getConstantData();
        }

        for(int i = 0; i < this.roles.size(); i++) {
            if(roles.get(i).getId().equals(newUser.getRoleId())) {
                user.setRole(roles.get(i));
                break;
            }
        }

        user.setRegistrationCode(randomCodeGenerator.randomUUID(15, 10, '-'));

        return user;
    }

    /***
     * Helper to validate given color
     *
     * @param color
     * @return
     */
    private boolean validateColor(String color) {
        if(color == null) {
            return false;
        }

        Pattern colorPattern = Pattern.compile("#([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8})");
        Matcher m = colorPattern.matcher(color);

        return m.matches();
    }
}
