package pt.lisomatrix.Sockets.controllers;

import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.*;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.repositories.*;
import pt.lisomatrix.Sockets.websocket.models.ThemeDAO;
import pt.lisomatrix.Sockets.websocket.models.UserDAO;

import java.security.Principal;
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
    private ClassesRepository classesRepository;

    @GetMapping("/user")
    @CrossOrigin
    public UserDAO getUser(Principal principal) throws Exception {

        Optional<User> foundUser = usersRepository.findById(Long.parseLong(principal.getName()));

        if(foundUser.isPresent()) {

            User user = foundUser.get();

            String role = user.getRole().getRole();

            UserDAO userDAO = new UserDAO();

            userDAO.setClassDirector(null);
            userDAO.setUserId(user.getUserId());

            Optional<UserSettings> foundUserSettings = userSettingsRepository.findFirstByUser(user);

            if(foundUserSettings.isPresent()) {
                UserSettings userSettings = foundUserSettings.get();

                userDAO.setUserThemeSettings(userSettings.getTheme());
            }

            if(role.equals(Roles.PROFESSOR.toString())) {

                Optional<Teacher> foundTeacher = teachersRepository.findFirstByUser(user);

                Teacher teacher = foundTeacher.get();

                Optional<Class> foundClass = classesRepository.findFirstByClassDirector(teacher);

                if(foundClass.isPresent()) {
                    userDAO.setClassDirector(true);
                }

                userDAO.setRoleEntityId(teacher.getTeacherId());
                userDAO.setId(teacher.getTeacherId());
                userDAO.setName(teacher.getName());


            } else if(role.equals(Roles.ALUNO.toString())) {

                Student student = studentsRepository.findFirstByUser(user).get();

                userDAO.setId(student.getStudentId());
                userDAO.setName(student.getName());
                userDAO.setRoleEntityId(student.getStudentId());

            } else if(role.equals(Roles.PARENTE.toString())) {

            }

            return userDAO;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found");
    }

    @PostMapping("/user/theme")
    @CrossOrigin
    public ThemeDAO updateTheme(@RequestBody ThemeDAO theme, Principal principal) {

        Optional<User> foundUser = usersRepository.findById(Long.parseLong(principal.getName()));

        if(foundUser.isPresent()) {

            User user = foundUser.get();

            Optional<UserSettings> foundUserSettings = userSettingsRepository.findFirstByUser(user);

            ThemeDAO currentTheme;

            UserSettings userSettings;

            if(foundUserSettings.isPresent()) {

                userSettings = foundUserSettings.get();

                currentTheme = new ThemeDAO();

                currentTheme.populate(userSettings.getTheme());

            } else {
                userSettings = new UserSettings();

                userSettings.setUser(user);
                currentTheme = new ThemeDAO();
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
