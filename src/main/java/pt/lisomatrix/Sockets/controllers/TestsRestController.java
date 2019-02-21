package pt.lisomatrix.Sockets.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.models.Test;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.TeachersRepository;
import pt.lisomatrix.Sockets.repositories.TestsRepository;
import pt.lisomatrix.Sockets.requests.models.MarkTest;
import pt.lisomatrix.Sockets.requests.models.RemoveTest;
import pt.lisomatrix.Sockets.websocket.models.Event;
import pt.lisomatrix.Sockets.websocket.models.TestDAO;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class TestsRestController {

    private TestsRepository testsRepository;

    private ClassesRepository classesRepository;

    private TeachersRepository teachersRepository;

    public TestsRestController(TestsRepository testsRepository, ClassesRepository classesRepository, TeachersRepository teachersRepository) {
        this.testsRepository = testsRepository;
        this.classesRepository = classesRepository;
        this.teachersRepository = teachersRepository;
    }

    @GetMapping("/class/{classId}/test")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasAnyRole('ROLE_ALUNO')")
    @CrossOrigin
    public List<TestDAO> getClassTests(@PathVariable("classId") long classId) {

        Optional<List<Test>> foundTests = testsRepository.findAllByTestClassAndDateAfter(new Class(classId), new Date());

        if(foundTests.isPresent()) {

            List<TestDAO> tests = populateDTAList(foundTests.get());

            return tests;

        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Tests not found"
            );
        }

    }

    @GetMapping("/teacher/{teacherId}/test")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<TestDAO> getTeacherTests(@PathVariable("teacherId") long teacherId, Principal principal) {

        Teacher teacher = teachersRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

        if(teacher.getTeacherId() == teacherId) {

            Optional<List<Test>> foundTests = testsRepository.findAllByTeacherAndDateAfter(teacher, new Date());

            if(foundTests.isPresent()) {

                List<TestDAO> tests = populateDTAList(foundTests.get());

                return tests;

            } else {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tests not found"
                );
            }

        } else {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Unauthorized"
            );
        }
    }

    @PostMapping("/class/{classId}/test")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public TestDAO markTest(@RequestBody MarkTest markTest, @PathVariable("classId") long classId, Principal principal) {

        Date date = new Date();

        if(markTest.getDate().compareTo(date) > 0) {

            Optional<Class> foundClass = classesRepository.findById(classId);

            if(foundClass.isPresent()) {

                Class aClass = foundClass.get();

                Teacher teacher = teachersRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

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

                        return testDAO;
                    } else {
                        throw new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "Unauthorized"
                        );
                    }

                } else {
                    throw new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED, "Unauthorized"
                    );
                }

            } else {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Class not found"
                );
            }

        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid date"
            );
        }

    }

    @DeleteMapping("class/{classId}/test/{testId}")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public TestDAO removeTest(@PathVariable("classId") long classId, @PathVariable("testId") long testId, Principal principal) {

        Optional<Test> foundTest = testsRepository.findById(testId);

        if(foundTest.isPresent()) {

            Test test = foundTest.get();

            Teacher teacher = teachersRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

            if(test.getTeacher().getTeacherId().equals(teacher.getTeacherId())) {

                testsRepository.delete(test);

                TestDAO testDAO = new TestDAO();

                testDAO.populate(test);

                return testDAO;
            } else {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Unauthorized"
                );
            }

        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Test not found"
            );
        }
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
