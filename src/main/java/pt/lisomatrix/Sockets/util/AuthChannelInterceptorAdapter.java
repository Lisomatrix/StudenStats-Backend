package pt.lisomatrix.Sockets.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Parent;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.models.User;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;
import pt.lisomatrix.Sockets.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {

    @Autowired
    private AuthenticationHelper authenticationHelper;

    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private TeachersRepository teachersRepository;

    @Autowired
    private ParentsRepository parentsRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private UsersRepository usersRepository;

    private static final String TOKEN_HEADER = "auth";


    private void SetStorage(String sessionId, String role, Long userId) {

        sessionHandler.addAttribute(sessionId, "role", role);

        Optional<User> user = usersRepository.findById(userId);

        if(user.isPresent()) {

            User u = user.get();

            u.setPassword("");

            sessionHandler.addAttribute(sessionId, "user", u);

        } else {
            sessionHandler.disconnectClient(sessionId);
        }

        if(role.equals(Roles.PROFESSOR.toString())) {

            Optional<Teacher> teacher = teachersRepository.findFirstByUser(new User(userId));

            if(teacher.isPresent()) {
                sessionHandler.addAttribute(sessionId, "teacher", teacher.get());

                Teacher teacher1 = teacher.get();

                Optional<Class> a = classesRepository.findFirstByClassDirector(teacher1);

                if(a.isPresent()) {
                    sessionHandler.addAttribute(sessionId, "class", a.get());
                }
            }


        } else if(role.equals(Roles.ALUNO.toString())) {

            Optional<Student> student = studentsRepository.findFirstByUser(new User(userId));

            if(student.isPresent()) {
                sessionHandler.addAttribute(sessionId, "student", student.get());

                Optional<Class> a = classesRepository.findFirstByStudents(student.get());

                if(a.isPresent()) {
                    sessionHandler.addAttribute(sessionId, "class", a.get());
                }
            }

        } else if(role.equals(Roles.PARENTE.toString())) {

            Optional<Parent> parent = parentsRepository.findFirstByUser(new User(userId));

            if(parent.isPresent()) {
                sessionHandler.addAttribute(sessionId, "parent", parent.get());
            }
        }

    }

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) throws AuthenticationException {

        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            final String token = accessor.getFirstNativeHeader(TOKEN_HEADER);

            Authenticated authenticated = authenticationHelper.authenticate(token);
            if(authenticated.getAuthenticated()) {

                if(sessionHandler.checkSameIP(accessor.getSessionId(), authenticated.getIp())) {

                    List<GrantedAuthority> authorities = new ArrayList<>();

                    authorities.add(new SimpleGrantedAuthority(authenticated.getRole()));

                    accessor.setUser(new UsernamePasswordAuthenticationToken(authenticated.getUserId(), null, authorities));

                    SetStorage(accessor.getSessionId(), authenticated.getRole(), authenticated.getUserId());

                } else {
                    sessionHandler.disconnectClient(accessor.getSessionId());
                    throw new AuthenticationExceptionImpl("IP not equal");
                }

            } else {
                sessionHandler.disconnectClient(accessor.getSessionId());
                throw new AuthenticationExceptionImpl("Invalid Token");
            }
        }

        return message;
    }
}
