package pt.lisomatrix.Sockets.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.models.Test;
import pt.lisomatrix.Sockets.repositories.*;
import pt.lisomatrix.Sockets.requests.models.MarkTest;
import pt.lisomatrix.Sockets.response.models.TestResponse;

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

    private ModulesRepository modulesRepository;

    public TestsRestController(TestsRepository testsRepository, ClassesRepository classesRepository, TeachersRepository teachersRepository, ModulesRepository modulesRepository) {
        this.testsRepository = testsRepository;
        this.classesRepository = classesRepository;
        this.teachersRepository = teachersRepository;
        this.modulesRepository = modulesRepository;
    }

    @GetMapping("/class/{classId}/test")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasAnyRole('ROLE_ALUNO')")
    @CrossOrigin
    public List<TestResponse> getClassTests(@PathVariable("classId") long classId) {

        Optional<List<Test>> foundTests = testsRepository.findAllByTestClass(new Class(classId));

        if(foundTests.isPresent()) {

            List<TestResponse> tests = populateDTAList(foundTests.get());

            return tests;

        } else {
            return new ArrayList<TestResponse>();
        }

    }

    @GetMapping("/teacher/{teacherId}/test")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<TestResponse> getTeacherTests(@PathVariable("teacherId") long teacherId, Principal principal) {

        Teacher teacher = teachersRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

        if(teacher.getTeacherId() == teacherId) {

            Optional<List<Test>> foundTests = testsRepository.findAllByTeacherAndDateAfter(teacher, new Date());

            if(foundTests.isPresent()) {

                List<TestResponse> tests = populateDTAList(foundTests.get());

                return tests;

            } else {
                return new ArrayList<TestResponse>();
            }

        } else {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Unauthorized"
            );
        }
    }

    @GetMapping("/student/{studentId}/test")
    @PreAuthorize("hasRole('ROLE_PARENT')")
    @CrossOrigin
    public List<TestResponse> getChildTests(@PathVariable("studentId") long studentId, Principal principal) {

        Optional<Class> foundClass = classesRepository.findFirstByParentUserIdAndStudentId(Long.parseLong(principal.getName()), studentId);

        if(foundClass.isPresent()) {

            Optional<List<Test>> foundTests = testsRepository.findAllByTestClass(foundClass.get());

            if(foundTests.isPresent()) {
                return populateDTAList(foundTests.get());
            }
        }

        return new ArrayList<>();
    }

    @PostMapping("/class/{classId}/test")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public TestResponse markTest(@RequestBody MarkTest markTest, @PathVariable("classId") long classId, Principal principal) {

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

                        //Optional<pt.lisomatrix.Sockets.models.Module> foundLastModule = modulesRepository.findLastModule(discipline.getDisciplineId(), aClass.getClassId());
                        Optional<pt.lisomatrix.Sockets.models.Module> foundLastModule = modulesRepository.findById(markTest.getModuleId());

                        if(foundLastModule.isPresent()) {
                            newTest.setTestClass(aClass);
                            newTest.setTeacher(teacher);
                            newTest.setDiscipline(discipline);
                            newTest.setDate(markTest.getDate());
                            newTest.setModule(foundLastModule.get());

                            Test savedTest = testsRepository.save(newTest);

                            TestResponse testResponse = new TestResponse();

                            testResponse.populate(savedTest);

                            return testResponse;
                        } else {
                            throw new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Module not found"
                            );
                        }
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
    public TestResponse removeTest(@PathVariable("classId") long classId, @PathVariable("testId") long testId, Principal principal) {

        Optional<Test> foundTest = testsRepository.findById(testId);

        if(foundTest.isPresent()) {

            Test test = foundTest.get();

            Teacher teacher = teachersRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

            if(test.getTeacher().getTeacherId().equals(teacher.getTeacherId())) {

                testsRepository.delete(test);

                TestResponse testResponse = new TestResponse();

                testResponse.populate(test);

                return testResponse;
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
    private List<TestResponse> populateDTAList(List<Test> tests) {

        List<TestResponse> testResponseList = new ArrayList<TestResponse>();


        for(int i = 0; i < tests.size(); i++) {
            TestResponse testResponse = new TestResponse();

            testResponse.populate(tests.get(i));

            testResponseList.add(testResponse);
        }

        return testResponseList;
    }

}
