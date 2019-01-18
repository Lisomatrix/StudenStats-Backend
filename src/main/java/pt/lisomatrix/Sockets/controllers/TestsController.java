package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.models.Test;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.TestsRepository;
import pt.lisomatrix.Sockets.requests.models.MarkTest;
import pt.lisomatrix.Sockets.requests.models.RemoveTest;
import pt.lisomatrix.Sockets.util.SessionHandler;
import pt.lisomatrix.Sockets.websocket.models.Event;
import pt.lisomatrix.Sockets.websocket.models.TestDAO;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class TestsController {

    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private TestsRepository testsRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @MessageMapping("/test")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasAnyRole('ROLE_ALUNO')")
    public Event getTests(StompHeaderAccessor accessor) {

        Class aClass = (Class) sessionHandler.getAttribute(accessor.getSessionId(), "class");

        if(aClass == null) {
            return new Event("BAD_REQUEST", null);
        }

        String role = (String) sessionHandler.getAttribute(accessor.getSessionId(), "role");

        List<Test> tests = null;

        if(role.equals(Roles.ALUNO.toString())) {

            Optional<List<Test>> foundTests = testsRepository.findAllByTestClassAndDateAfter(aClass, new Date());

            if(foundTests.isPresent()) {

                tests = foundTests.get();
            }

        } else if(role.equals(Roles.PROFESSOR.toString())) {

            Teacher teacher = (Teacher) sessionHandler.getAttribute(accessor.getSessionId(), "teacher");

            Optional<List<Test>> foundTests = testsRepository.findAllByTeacherAndDateAfter(teacher, new Date());

            if(foundTests.isPresent()) {

                tests = foundTests.get();
            }
        }

        if(tests != null) {

            List<TestDAO> testDAOList = populateDTAList(tests);

            return new Event("GET_TESTS", testDAOList);
        }

        return new Event("BAD_REQUEST", null);
    }

    @MessageMapping("/test/create")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Event markTest(MarkTest markTest, StompHeaderAccessor accessor) {

        Date date = new Date();

        if(markTest.getDate().compareTo(date) > 0) {

            Optional<Class> foundClass = classesRepository.findById(markTest.getClassId());

            if(foundClass.isPresent()) {

                Class aClass = foundClass.get();

                Teacher teacher = (Teacher) sessionHandler.getAttribute(accessor.getSessionId(), "teacher");

                Boolean isAllowedClass = false;


                for(int i = 0; i < aClass.getTeachers().size(); i++) {
                    if(aClass.getTeachers().get(i).getTeacherId().equals(teacher.getTeacherId())) {
                        isAllowedClass = true;
                        break;
                    }
                }

                if(isAllowedClass) {

                    Boolean isAllowedDiscipline = false;

                    Discipline discipline = null;

                    for(int i = 0; i < aClass.getDisciplines().size(); i++) {
                        if(aClass.getDisciplines().get(i).getDisciplineId().equals(markTest.getDisciplineId())) {
                            isAllowedDiscipline = true;
                            discipline = aClass.getDisciplines().get(i);
                            break;
                        }
                    }

                    if(isAllowedDiscipline) {

                        Test newTest = new Test();

                        newTest.setTestClass(aClass);
                        newTest.setTeacher(teacher);
                        newTest.setDiscipline(discipline);
                        newTest.setDate(markTest.getDate());
                        //newTest.setModule(); // TODO NEED MODULES IN THIS

                        Test savedTest = testsRepository.save(newTest);

                        TestDAO testDAO = new TestDAO();

                        testDAO.populate(savedTest);

                        return new Event("MARK_TEST", testDAO);
                    }
                }
            }
        }

        return new Event("BAD_REQUEST", null);
    }

    @MessageMapping("/test/delete")
    @SendToUser("/queue/reply")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public Event removeTest(RemoveTest removeTest, StompHeaderAccessor accessor) {

        Optional<Test> foundTest = testsRepository.findById(removeTest.getTestId());

        if(foundTest.isPresent()) {

            Test test = foundTest.get();

            Teacher teacher = (Teacher) sessionHandler.getAttribute(accessor.getSessionId(), "teacher");

            if(test.getTeacher().getTeacherId().equals(teacher.getTeacherId())) {

                testsRepository.delete(test);

                TestDAO testDAO = new TestDAO();

                testDAO.setTestId(test.getTestId());

                return new Event("TEST_REMOVE", testDAO);
            }
        }

        return new Event("BAD_REQUEST", null);
    }

    /***
     * Helper to convert from List<Test> to List<TestDAO>
     *
     * @param tests
     * @return
     */
    private List<TestDAO> populateDTAList(List<Test> tests) {

        List<TestDAO> testDAOList = new ArrayList<TestDAO>();


        for(int i = 0; i < tests.size(); i++) {
            TestDAO testDAO = new TestDAO();

            testDAO.populate(tests.get(i));

            testDAOList.add(testDAO);
        }

        return testDAOList;
    }
}
