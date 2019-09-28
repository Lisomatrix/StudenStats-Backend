package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.*;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Module;
import pt.lisomatrix.Sockets.repositories.*;
import pt.lisomatrix.Sockets.requests.models.UpdateModuleGrade;
import pt.lisomatrix.Sockets.requests.models.UpdateTestGrade;
import pt.lisomatrix.Sockets.response.models.ModuleGradeResponse;
import pt.lisomatrix.Sockets.response.models.TestGradeResponse;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class GradesRestController {

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private ModuleGradesRepository moduleGradesRepository;

    @Autowired
    private TestGradesRepository testGradesRepository;

    @Autowired
    private TestsRepository testsRepository;

    @Autowired
    private ModulesRepository modulesRepository;

    @GetMapping("/class/{classId}/module/grade")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<ModuleGradeResponse> getClassModuleGrades(@PathVariable("classId") Long classId, Principal principal) throws Exception {

        Optional<List<Class>> foundClasses = classesRepository.findAllByTeachersUserIdIsIn(Long.parseLong(principal.getName()));

        if(foundClasses.isPresent()) {
            List<Class> classes = foundClasses.get();

            Class aClass = null;

            for(int i = 0; i < classes.size(); i++) {
                if(classes.get(i).getClassId().equals(classId)) {
                    aClass = classes.get(i);
                    break;
                }
            }

            if(aClass != null) {

                Optional<List<ModuleGrade>> foundModuleGrades = moduleGradesRepository.findAllByClass(aClass.getClassId());

                if(foundModuleGrades.isPresent()) {

                    List<ModuleGradeResponse> moduleGradeResponses = populateModuleGradeDAOList(foundModuleGrades.get());

                    return moduleGradeResponses;
                }

                return new ArrayList<>();
            }

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Class not found");
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Class not found");
    }

    @GetMapping("/student/module/grade")
    @PreAuthorize("hasRole('ROLE_ALUNO')")
    @CrossOrigin
    public List<ModuleGradeResponse> getStudentModuleGrades(Principal principal) throws Exception {

        Student student = studentsRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

        Optional<List<ModuleGrade>> foundModuleGrades = moduleGradesRepository.findAllByStudentId(student.getStudentId());

        if(foundModuleGrades.isPresent()) {

            List<ModuleGradeResponse> moduleGradeResponses = populateModuleGradeDAOList(foundModuleGrades.get());

            return moduleGradeResponses;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Module grades not found");
    }

    @GetMapping("/student/{studentId}/module/grade")
    @PreAuthorize("hasRole('ROLE_PARENT')")
    @CrossOrigin
    public List<ModuleGradeResponse> getChildModuleGrades(@PathVariable("studentId") long studentId, Principal principal) {

        Optional<List<Student>> foundStudents = studentsRepository.findAllByParentUserId(Long.parseLong(principal.getName()));

        if(foundStudents.isPresent()) {
            List<Student> students = foundStudents.get();

            for(int i = 0; i < students.size(); i++) {
                if(students.get(i).getStudentId().equals(studentId)) {

                    Optional<List<ModuleGrade>> foundModuleGrades = moduleGradesRepository.findAllByStudentId(students.get(i).getStudentId());

                    if(foundModuleGrades.isPresent()) {

                        List<ModuleGradeResponse> moduleGradeResponses = populateModuleGradeDAOList(foundModuleGrades.get());

                        return moduleGradeResponses;
                    } else {
                        return new ArrayList<>();
                    }
                }
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Module grades not found");
    }

    @GetMapping("/student/{studentId}/test/grade")
    @PreAuthorize("hasRole('ROLE_PARENT')")
    @CrossOrigin
    public List<TestGradeResponse> getChildTestGrades(@PathVariable("studentId") long studentId, Principal principal) {
        Optional<List<Student>> foundStudents = studentsRepository.findAllByParentUserId(Long.parseLong(principal.getName()));

        if(foundStudents.isPresent()) {
            List<Student> students = foundStudents.get();

            for(int i = 0; i < students.size(); i++) {
                if(students.get(i).getStudentId().equals(studentId)) {

                    Optional<List<TestGrade>> foundTesteGrades = testGradesRepository.findAllByStudent(students.get(i));

                    if(foundTesteGrades.isPresent()) {

                        List<TestGradeResponse> testGradeResponses = populateTestGradeDAOList(foundTesteGrades.get());

                        return testGradeResponses;
                    } else {
                        return new ArrayList<>();
                    }
                }
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Test grades not found");
    }

    @PostMapping("/module/{moduleId}/grade")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<ModuleGradeResponse> updateModuleGrade(@PathVariable("moduleId") Long moduleId, @RequestBody List<UpdateModuleGrade> updateModuleGrades, Principal principal) {

        Optional<Module> foundModule = modulesRepository.findById(moduleId);

        if(foundModule.isPresent()) {

            List<Long> studentIds = new ArrayList<>();

            for(int i = 0; i < updateModuleGrades.size(); i++) {
                studentIds.add(updateModuleGrades.get(i).getStudentId());
            }

            Optional<List<ModuleGrade>> foundExistingGrades = moduleGradesRepository.findAllByModuleIdAndStudentIds(moduleId, studentIds);

            List<ModuleGrade> moduleGrades = new ArrayList<>();
            List<ModuleGrade> createdModuleGrades = null;

            if(foundExistingGrades.isPresent()) {
                moduleGrades = updateModuleGrade(foundExistingGrades.get(), updateModuleGrades);
                createdModuleGrades = getNonExistingModuleGrades(moduleGrades, updateModuleGrades, moduleId);
            } else {
                createdModuleGrades = createModuleGrades(updateModuleGrades, moduleId);
            }

            List<ModuleGrade> saved = new ArrayList<>();

            for(int i = 0; i < moduleGrades.size(); i++) {
                ModuleGrade temp = moduleGrades.get(i);
                moduleGradesRepository.updateExistingModuleGrades(temp.getGrade(), temp.getModuleGradeId());

                temp = moduleGradesRepository.findById(temp.getModuleGradeId()).get();

                saved.add(temp);
            }

            if(createdModuleGrades != null) {

                for(int i = 0; i < createdModuleGrades.size(); i++) {
                    saved.add(moduleGradesRepository.save(createdModuleGrades.get(i)));
                }
            }

            return populateModuleGradeDAOList(saved);
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Module not found");
    }

    @GetMapping("/test/{testId}/grade")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<TestGradeResponse> getTestGrades(@PathVariable("testId") long testId, Principal principal) {

        Test test = new Test();

        test.setTestId(testId);

        Optional<List<TestGrade>> foundTestGrades = testGradesRepository.findAllByTest(test);

        if(foundTestGrades.isPresent()) {
            return populateTestGradeDAOList(foundTestGrades.get());
        }

        return new ArrayList<TestGradeResponse>();
    }

    @GetMapping("/class/{classId}/module/{moduleId}/grade")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<TestGradeResponse> getClassAndModuleTestGrades(@PathVariable("classId") long classId, @PathVariable("moduleId") long moduleId) {

        Optional<List<TestGrade>> foundTestGrades = testGradesRepository.findAllByClassAndModule(classId, moduleId);

        if(foundTestGrades.isPresent()) {
            return populateTestGradeDAOList(foundTestGrades.get());
        }

        return new ArrayList<TestGradeResponse>();
    }

    @GetMapping("/student/grade")
    @PreAuthorize("hasRole('ROLE_ALUNO')")
    @CrossOrigin
    public List<TestGradeResponse> getStudentTestGrades(Principal principal) {

        Student student = studentsRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

        Optional<List<TestGrade>> foundTestGrades = testGradesRepository.findAllByStudent(student);

        if(foundTestGrades.isPresent()) {
            return populateTestGradeDAOList(foundTestGrades.get());
        } else {
            return new ArrayList<>();
        }
    }

    @PostMapping("/test/{testId}/grade")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public ResponseEntity<?> updateTestGrades(@PathVariable("testId") long testId, Principal principal, @RequestBody List<UpdateTestGrade> updateTestGradeList) {


        Optional<Test> foundTest = testsRepository.findFirstByTestIdAndTeacherId(Long.parseLong(principal.getName()), testId);


        if(foundTest.isPresent()) {

            Test test = foundTest.get();

            long[] studentIds = new long[updateTestGradeList.size()];
            for(int i = 0; i < updateTestGradeList.size(); i++) {
                studentIds[i] = updateTestGradeList.get(i).getStudentId();
            }

            Optional<List<TestGrade>> foundExistingGrades = testGradesRepository.findAllByStudentIdAndTestId(studentIds, test.getTestId());

            List<TestGrade> testGrades = new ArrayList<>();
            List<TestGrade> createdTestGrades = null;

            if(foundExistingGrades.isPresent()) {
                testGrades = updateTestGrades(foundExistingGrades.get(), updateTestGradeList);
                createdTestGrades = getNonExistingTestGrades(testGrades, updateTestGradeList);
            } else {
                createdTestGrades = createTestGrades(updateTestGradeList);
            }

            List<TestGrade> saved = new ArrayList<>();

            for(int i = 0; i < testGrades.size(); i++) {
                TestGrade temp = testGrades.get(i);
                testGradesRepository.updateExistingTestGrades(temp.getGrade(), temp.getTestGradeId());

                temp = testGradesRepository.findById(temp.getTestGradeId()).get();

                saved.add(temp);
            }

            if(createdTestGrades != null) {

                for(int i = 0; i < createdTestGrades.size(); i++) {
                    saved.add(testGradesRepository.save(createdTestGrades.get(i)));
                }
            }

            return ResponseEntity.ok(populateTestGradeDAOList(saved));
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Test not found");
    }

    private List<TestGradeResponse> populateTestGradeDAOList(List<TestGrade> testGrades) {

        List<TestGradeResponse> testGradeResponses = new ArrayList<>();

        for(int i = 0; i < testGrades.size(); i++) {

            TestGradeResponse testGradeResponse = new TestGradeResponse();

            testGradeResponse.populate(testGrades.get(i));

            testGradeResponses.add(testGradeResponse);
        }

        return testGradeResponses;
    }

    private List<ModuleGradeResponse> populateModuleGradeDAOList(List<ModuleGrade> moduleGrades) {

        List<ModuleGradeResponse> moduleGradeResponses = new ArrayList<>();

        for(int i = 0; i < moduleGrades.size(); i++) {

            ModuleGrade moduleGrade = moduleGrades.get(i);

            ModuleGradeResponse moduleGradeResponse = new ModuleGradeResponse();

            moduleGradeResponse.setModuleGrade(moduleGrade.getGrade());
            moduleGradeResponse.setModuleGradeId(moduleGrade.getModuleGradeId());
            moduleGradeResponse.setModuleId(moduleGrade.getModule().getModuleId());
            moduleGradeResponse.setStudentId(moduleGrade.getStudent().getStudentId());

            moduleGradeResponses.add(moduleGradeResponse);
        }

        return moduleGradeResponses;
    }

    private List<TestGrade> updateTestGrades(List<TestGrade> testGrades, List<UpdateTestGrade> updateTestGrades) {

        List<TestGrade> testGradeList = new ArrayList<>();

        for(int i = 0; i < testGrades.size(); i++) {

            for(int x = 0; x < updateTestGrades.size(); x++) {

                TestGrade testGrade = testGrades.get(i);
                UpdateTestGrade updateTestGrade = updateTestGrades.get(x);

                if(testGrade.getTest().getTestId() == updateTestGrade.getTestId() && testGrade.getStudent().getStudentId().equals(updateTestGrade.getStudentId())) {

                   testGrade.setGrade(updateTestGrade.getGrade());

                   testGradeList.add(testGrade);
                }
            }
        }

        return testGradeList;
    }

    private List<TestGrade> getNonExistingTestGrades(List<TestGrade> testGrades, List<UpdateTestGrade> updateTestGrades) {

        List<TestGrade> testGradeList = new ArrayList<>();

        for(int i = 0; i < testGrades.size(); i++) {

            boolean exists = false;
            TestGrade testGrade = testGrades.get(i);
            UpdateTestGrade nonExistingUpdateTestGrade = new UpdateTestGrade();

            for(int x = 0; x < updateTestGrades.size(); x++) {

                UpdateTestGrade updateTestGrade = updateTestGrades.get(x);

                if(testGrade.getTest().getTestId() == updateTestGrade.getTestId() && testGrade.getStudent().getStudentId().equals(updateTestGrade.getStudentId())) {
                    exists = true;
                    break;
                }

                if(x == updateTestGrades.size() - 1) {
                    nonExistingUpdateTestGrade = updateTestGrade;
                }
            }

            if(!exists) {
                TestGrade temp = new TestGrade();

                temp.setGrade(nonExistingUpdateTestGrade.getGrade());

                Student student = new Student();
                student.setStudentId(nonExistingUpdateTestGrade.getStudentId());

                temp.setStudent(student);

                Test test = new Test();
                test.setTestId(nonExistingUpdateTestGrade.getTestId());

                temp.setTest(test);

                testGradeList.add(temp);
            }
        }

        return testGradeList;
    }

    private List<TestGrade> createTestGrades(List<UpdateTestGrade> updateTestGradeList) {

        List<TestGrade> testGradeList = new ArrayList<>();

        for(int i = 0; i < updateTestGradeList.size(); i++) {

            UpdateTestGrade updateTestGrade = updateTestGradeList.get(i);

            TestGrade temp = new TestGrade();

            temp.setGrade(updateTestGrade.getGrade());

            Student student = new Student();
            student.setStudentId(updateTestGrade.getStudentId());

            temp.setStudent(student);

            Test test = new Test();
            test.setTestId(updateTestGrade.getTestId());

            temp.setTest(test);

            testGradeList.add(temp);
        }

        return testGradeList;
    }

    private List<ModuleGrade> updateModuleGrade(List<ModuleGrade> moduleGrades, List<UpdateModuleGrade> updateModuleGrades) {

        List<ModuleGrade> moduleGradesList = new ArrayList<>();

        for(int x = 0; x < moduleGrades.size(); x++) {

            ModuleGrade moduleGrade = moduleGrades.get(x);

            for(int i = 0; i < updateModuleGrades.size(); i++) {

                UpdateModuleGrade updateModuleGrade = updateModuleGrades.get(i);

                if(moduleGrade.getStudent().getStudentId() == updateModuleGrade.getStudentId()) {

                    moduleGrade.setGrade(updateModuleGrade.getModuleGrade());

                    moduleGradesList.add(moduleGrade);
                }
            }
        }

        return moduleGradesList;
    }

    private List<ModuleGrade> getNonExistingModuleGrades(List<ModuleGrade> moduleGrades, List<UpdateModuleGrade> updateModuleGrades, long moduleId) {

        List<ModuleGrade> moduleGradeList = new ArrayList<>();

        for(int i = 0; i < moduleGrades.size(); i++) {

            boolean exists = false;
            ModuleGrade moduleGrade = moduleGrades.get(i);
            UpdateModuleGrade nonExistingUpdateModuleGrade = new UpdateModuleGrade();

            for(int x = 0; x < updateModuleGrades.size(); x++) {

                UpdateModuleGrade updateModuleGrade = updateModuleGrades.get(x);

                if(moduleGrade.getStudent().getStudentId().equals(updateModuleGrade.getStudentId())) {
                    exists = true;
                    break;
                }

                if(x == updateModuleGrades.size() - 1) {
                    nonExistingUpdateModuleGrade = updateModuleGrade;
                }
            }

            if(!exists) {
                ModuleGrade temp = new ModuleGrade();

                temp.setGrade(nonExistingUpdateModuleGrade.getModuleGrade());

                Student student = new Student();
                student.setStudentId(nonExistingUpdateModuleGrade.getStudentId());

                temp.setStudent(student);

                Module module = new Module();
                module.setModuleId(moduleId);

                temp.setModule(module);

                moduleGradeList.add(temp);
            }
        }

        return moduleGradeList;
    }

    private List<ModuleGrade> createModuleGrades(List<UpdateModuleGrade> updateModuleGradeList, long moduleId) {

        List<ModuleGrade> moduleGradeList = new ArrayList<>();

        for(int i = 0; i < updateModuleGradeList.size(); i++) {

            UpdateModuleGrade updateModuleGrade = updateModuleGradeList.get(i);

            ModuleGrade temp = new ModuleGrade();

            Student student = new Student();
            student.setStudentId(updateModuleGrade.getStudentId());

            temp.setStudent(student);

            Module module = new Module();
            module.setModuleId(moduleId);

            temp.setModule(module);

            moduleGradeList.add(temp);
        }

        return moduleGradeList;
    }
}
