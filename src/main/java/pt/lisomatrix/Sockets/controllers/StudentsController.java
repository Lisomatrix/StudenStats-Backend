package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.requests.models.GetStudents;
import pt.lisomatrix.Sockets.util.SessionHandler;
import pt.lisomatrix.Sockets.websocket.models.Event;
import pt.lisomatrix.Sockets.websocket.models.StudentDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class StudentsController {

    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private ClassesRepository classesRepository;

    @MessageMapping("/student")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Event getStudents(GetStudents getStudents, StompHeaderAccessor accessor) {

        Optional<Class> foundClass = classesRepository.findById(getStudents.getClassId());

        if(foundClass.isPresent()) {

            Teacher teacher = (Teacher) sessionHandler.getAttribute(accessor.getSessionId(), "teacher");

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

                return new Event("GET_STUDENTS", studentDAOList);
            }
        }

        return new Event("BAD_REQUEST", null);
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
