package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.models.User;
import pt.lisomatrix.Sockets.models.UserSettings;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.UserSettingsRepository;
import pt.lisomatrix.Sockets.util.SessionHandler;
import pt.lisomatrix.Sockets.websocket.models.Event;
import pt.lisomatrix.Sockets.websocket.models.ThemeDAO;
import pt.lisomatrix.Sockets.websocket.models.UserDAO;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class UsersController {

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private SessionHandler sessionHandler;

    @MessageMapping("/user")
    @SendToUser("/queue/reply")
    public Event getUser(StompHeaderAccessor accessor) {

        String role = (String) sessionHandler.getAttribute(accessor.getSessionId(), "role");
        User user = (User) sessionHandler.getAttribute(accessor.getSessionId(), "user");

        UserDAO userDAO = new UserDAO();

        userDAO.setClassDirector(null);
        userDAO.setUserId(user.getUserId());

        Optional<UserSettings> foundUserSettings = userSettingsRepository.findFirstByUser(user);

        if(foundUserSettings.isPresent()) {

            UserSettings userSettings = foundUserSettings.get();

            userDAO.setUserThemeSettings(userSettings.getTheme());

        }

        if(role.equals(Roles.PROFESSOR.toString())) {

            Teacher teacher = (Teacher) sessionHandler.getAttribute(accessor.getSessionId(), "teacher");

            Optional<Class> foundClass = classesRepository.findFirstByClassDirector(teacher);

            if(foundClass.isPresent()) {
                userDAO.setClassDirector(true);
            }

            userDAO.setId(teacher.getTeacherId());
            userDAO.setName(teacher.getName());
            userDAO.setRoleEntityId(teacher.getTeacherId());


        } else if(role.equals(Roles.ALUNO.toString())) {

            Student student = (Student) sessionHandler.getAttribute(accessor.getSessionId(), "student");

            userDAO.setId(student.getStudentId());
            userDAO.setName(student.getName());
            userDAO.setRoleEntityId(student.getStudentId());

        } else if(role.equals(Roles.PARENTE.toString())) {

        }


        return new Event("GET_USER", userDAO);
    }

    @MessageMapping("/user/theme/update")
    @SendToUser("/queue/reply")
    public Event updateUserTheme(ThemeDAO theme, StompHeaderAccessor accessor) {

        User user = (User) sessionHandler.getAttribute(accessor.getSessionId(), "user");

        Optional<UserSettings> foundUserSettings = userSettingsRepository.findFirstByUser(user);

        if(foundUserSettings.isPresent()) {

            UserSettings userSettings = foundUserSettings.get();

            ThemeDAO currentTheme = new ThemeDAO();

            currentTheme.populate(userSettings.getTheme());

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

            return new Event("THEME_UPDATE", currentTheme);
        }

        return new Event("BAD_REQUEST", null);
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
