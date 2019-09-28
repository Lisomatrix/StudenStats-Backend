package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Course;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.repositories.*;
import pt.lisomatrix.Sockets.requests.models.NewClass;
import pt.lisomatrix.Sockets.requests.models.NewCourse;
import pt.lisomatrix.Sockets.response.models.ClassResponse;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
public class ClassesRestController {

    private List<Course> courses = null;

    @PostConstruct
    public void getRepeatedData() {
        courses = coursesRepository.findAll();
    }

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private TeachersRepository teachersRepository;

    @GetMapping("/class")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public List<ClassResponse> getClasses(Principal principal) {

        Optional<List<Class>> foundClasses = classesRepository.findAllByTeachersUserIdIsIn(Long.parseLong(principal.getName()));

        if(foundClasses.isPresent()) {

            return populateDTAList(foundClasses.get());
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Classes not found");
    }

    @PostMapping("/class/{classId}/user/{studentUserId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ResponseEntity<?> addStudentByUserId(@PathVariable long classId, @PathVariable long studentUserId) {

        Optional<Class> foundClass = classesRepository.findById(classId);

        if(foundClass.isPresent()) {

            Class aClass = foundClass.get();

            Optional<Student> foundStudent = studentsRepository.findFirstByUserId(studentUserId);

            if(foundStudent.isPresent()) {

                Student student = foundStudent.get();

                Optional<Class> foundStudentClass = classesRepository.findFirstByStudentsIn(student);

                if(foundStudentClass.isPresent()) {
                    Class studentClass = foundStudentClass.get();

                    List<Student> students = studentClass.getStudents();

                    students.remove(student);

                    studentClass.setStudents(students);

                    classesRepository.save(studentClass);
                }

                List<Student> students = aClass.getStudents();

                students.add(student);

                aClass.setStudents(students);

                classesRepository.save(aClass);

                return ResponseEntity.ok(classesRepository.findAll());
            }

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Student not found");
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Class not found");
    }

    @PostMapping("/class/{classId}/student/{studentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ResponseEntity<?> addStudentByStudentId(@PathVariable long classId, @PathVariable long studentId) {

        Optional<Class> foundClass = classesRepository.findById(classId);

        if(foundClass.isPresent()) {

            Class aClass = foundClass.get();

            Optional<Student> foundStudent = studentsRepository.findById(studentId);

            if(foundStudent.isPresent()) {

                Student student = foundStudent.get();

                Optional<Class> foundStudentClass = classesRepository.findFirstByStudentsIn(student);

                if(foundStudentClass.isPresent()) {
                    Class studentClass = foundStudentClass.get();

                    List<Student> students = studentClass.getStudents();

                    students.remove(student);

                    studentClass.setStudents(students);

                    classesRepository.save(studentClass);
                }

                List<Student> students = aClass.getStudents();

                students.add(student);

                aClass.setStudents(students);

                classesRepository.save(aClass);

                ClassResponse classResponse = new ClassResponse();

                classResponse.populate(aClass);

                return ResponseEntity.ok(classResponse);
            }

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Student not found");
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Class not found");
    }

    @DeleteMapping("/class/{classId}/student/{studentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ResponseEntity<?> removeStudentByStudentId(@PathVariable long classId, @PathVariable long studentId) {

        Optional<Class> foundClass = classesRepository.findById(classId);

        if(foundClass.isPresent()) {

            Class aClass = foundClass.get();

            Optional<Student> foundStudent = studentsRepository.findById(studentId);

            if(foundStudent.isPresent()) {

                Student student = foundStudent.get();

                List<Student> students = aClass.getStudents();

                students.remove(student);

                aClass.setStudents(students);

                classesRepository.save(aClass);

                ClassResponse classResponse = new ClassResponse();

                classResponse.populate(aClass);

                return ResponseEntity.ok(classResponse);
            }

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Student not found");
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Class not found");
    }

    @PutMapping("/admin/class/{classId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ClassResponse updateClass(@PathVariable long classId, @RequestBody NewClass newClass) {

        Optional<Class> foundClass = classesRepository.findById(classId);

        if(foundClass.isPresent()) {

            Class aClass = foundClass.get();

            aClass.setName(newClass.getName());
            aClass.setYear(newClass.getYear());
            aClass.setCourse(findCourseById(newClass.getCourseId()));

            if(!aClass.getClassDirector().getTeacherId().equals(newClass.getTeacherId())) {

                Optional<Teacher> foundTeacher = teachersRepository.findById(newClass.getTeacherId());

                if(foundTeacher.isPresent()) {

                    aClass.setClassDirector(foundTeacher.get());
                }

                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found!");
            }

            classesRepository.save(aClass);

            ClassResponse classResponse = new ClassResponse();

            classResponse.populate(aClass);

            return classResponse;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found!");
    }

    @PostMapping("/admin/class")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ClassResponse addClass(@RequestBody NewClass newClass) {

        Optional<Teacher> foundTeacher = teachersRepository.findById(newClass.getTeacherId());
        Optional<Course> foundCourse = coursesRepository.findById(newClass.getCourseId());

        if(foundTeacher.isPresent() && foundCourse.isPresent()) {
            Class createClass = new Class();

            createClass.setClassDirector(foundTeacher.get());
            createClass.setCourse(foundCourse.get());
            createClass.setName(newClass.getName());
            createClass.setYear(newClass.getYear());

            Class createdClass = classesRepository.save(createClass);

            ClassResponse classResponse = new ClassResponse();

            classResponse.populate(createdClass );

            return classResponse;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Teacher not found");
    }

    @DeleteMapping("/admin/class/{classId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ResponseEntity<?> removeClass(@PathVariable long classId) {

        Optional<Class> foundClass = classesRepository.findById(classId);

        if(foundClass.isPresent()) {
            Class aClass = foundClass.get();

            aClass.setActive(false);

            classesRepository.save(aClass);

            return ResponseEntity.ok().build();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Class not found");
    }

    @GetMapping("/admin/class")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public List<ClassResponse> getAdminClasses() {
        List<Class> foundClasses = classesRepository.findAllByActive(true);

        return populateDTAList(foundClasses);
    }

    @GetMapping("/admin/course")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public List<Course> getCourses() {

        if(courses == null) {
            courses = coursesRepository.findAll();
        }

        return courses;
    }

    @PutMapping("/admin/course/{courseId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public Course updateCourse(@PathVariable long courseId, @RequestBody NewCourse newCourse) {

        Optional<Course> foundCourse = coursesRepository.findById(courseId);

        if(foundCourse.isPresent()) {

            Course course = foundCourse.get();

            course.setName(newCourse.getName());

            coursesRepository.save(course);

            this.courses = coursesRepository.findAll();

            return course;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
    }

    @PostMapping("/admin/course")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public Course addCourse(@RequestBody NewCourse newCourse) {

        Course course = new Course();

        course.setName(newCourse.getName());

        course = coursesRepository.save(course);

        this.courses.add(course);

        return course;
    }

    @GetMapping("/student/{studentId}/class")
    @PreAuthorize("hasRole('ROLE_PARENT')")
    @CrossOrigin
    public ClassResponse getChildClass(@PathVariable("studentId") long studentId, Principal principal) {

        Optional<List<Student>> foundStudents = studentsRepository.findAllByParentUserId(Long.parseLong(principal.getName()));

        if(foundStudents.isPresent()) {

            Student requestedStudent = null;

            for(int i = 0; i < foundStudents.get().size(); i++) {
                if(foundStudents.get().get(i).getStudentId().equals(studentId)) {
                    requestedStudent = foundStudents.get().get(i);
                    break;
                }
            }

            if(requestedStudent != null) {
                Optional<Class> foundClass = classesRepository.findFirstByStudentsIn(requestedStudent);

                if(foundClass.isPresent()) {

                    ClassResponse classResponse = new ClassResponse();

                    classResponse.populate(foundClass.get());

                    return classResponse;
                }

                return new ClassResponse();
            }

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Student not found");
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Student not found");
    }

    @GetMapping("/student/class")
    @PreAuthorize("hasRole('ROLE_ALUNO')")
    @CrossOrigin
    public ClassResponse getStudentClass(Principal principal) {

        Student student = studentsRepository.findFirstByUserId(Long.parseLong(principal.getName())).get();

        Optional<Class> foundClass = classesRepository.findFirstByStudentsIn(student);

        if(foundClass.isPresent()) {
            ClassResponse classResponse = new ClassResponse();

            classResponse.populate(foundClass.get());

            return classResponse;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found");
    }

    @GetMapping("/class/async")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public DeferredResult<ResponseEntity<?>> getClassesAsync(Principal principal) throws Exception {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        CompletableFuture.supplyAsync(() -> classesRepository.findAllByTeachersUserIdIsIn(Long.parseLong(principal.getName())))
                .thenApplyAsync((foundClasses) -> {

                    if(foundClasses.isPresent()) {

                        deferredResult.setResult(ResponseEntity.ok(populateDTAList(foundClasses.get())));
                        return null;

                    } else {
                        return null;
                    }
                });

        return deferredResult;
    }

    private Course findCourseById(long courseId) {

        for(int i = 0; i < this.courses.size(); i++) {
            if(courses.get(i).getCourseId() == courseId) {
                return courses.get(i);
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
    }

    /***
     * Helper to convert a List<Class> into List<ClassDAO>
     *
     * @param classes
     * @return
     */
    private List<ClassResponse> populateDTAList(List<Class> classes) {

        List<ClassResponse> classResponseList = new ArrayList<ClassResponse>();

        for(int i = 0; i < classes.size(); i++) {
            ClassResponse classResponse = new ClassResponse();

            classResponse.populate(classes.get(i));

            classResponseList.add(classResponse);
        }

        return classResponseList;
    }
}
