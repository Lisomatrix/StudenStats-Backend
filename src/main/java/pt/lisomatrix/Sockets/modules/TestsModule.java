package pt.lisomatrix.Sockets.modules;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.*;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.DisciplinesRepository;
import pt.lisomatrix.Sockets.repositories.TeachersRepository;
import pt.lisomatrix.Sockets.repositories.TestsRepository;
import pt.lisomatrix.Sockets.requests.models.MarkTest;
import pt.lisomatrix.Sockets.requests.models.RemoveTest;
import pt.lisomatrix.Sockets.websocket.models.TestDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/***
 * Listens and handles all WebSocket messages related to Tests
 *
 */
@Component
public class TestsModule {

    /***
     * WebSockets server object
     */
    private SocketIOServer server;

    private TestsRepository testsRepository;

    /***
     * REDIS Users Storage repository to get in ram info
     */
    private RedisUsersStorageRepository redisUsersStorageRepository;

    /***
     * Students Repository to get info
     */
    private TeachersRepository teachersRepository;

    /***
     * Disciplines Repository to get info
     */
    private DisciplinesRepository disciplinesRepository;

    /***
     * Classes Repository to get info
     */
    private ClassesRepository classesRepository;

    /***
     * Initialize all class dependencies and
     * Start listening to specific WebSocket messages
     *
     * @param server
     * @param testsRepository
     * @param redisUsersStorageRepository
     * @param classesRepository
     * @param disciplinesRepository
     * @param teachersRepository
     */
    @Autowired
    public TestsModule(SocketIOServer server, TestsRepository testsRepository, RedisUsersStorageRepository redisUsersStorageRepository, ClassesRepository classesRepository, DisciplinesRepository disciplinesRepository, TeachersRepository teachersRepository) {
        this.server = server;
        this.testsRepository = testsRepository;
        this.redisUsersStorageRepository = redisUsersStorageRepository;
        this.classesRepository  = classesRepository;
        this.teachersRepository = teachersRepository;
        this.disciplinesRepository = disciplinesRepository;

        // Listen for the following events
        this.server.addEventListener("GET_TESTS", Test.class, new DataListener<Test>() {
            @Override
            public void onData(SocketIOClient client, Test test, AckRequest ackRequest) throws Exception {
                getTests(client, ackRequest, client.getSessionId().toString());
            }
        });

        this.server.addEventListener("MARK_TEST", MarkTest.class, new DataListener<MarkTest>() {
            @Override
            public void onData(SocketIOClient client, MarkTest test, AckRequest ackRequest) throws Exception {
                markTest(client, ackRequest, client.getSessionId().toString(), test);
            }
        });

        this.server.addEventListener("TEST_REMOVE", RemoveTest.class, new DataListener<RemoveTest>() {
            @Override
            public void onData(SocketIOClient socketIOClient, RemoveTest removeTest, AckRequest ackRequest) throws Exception {
                removeTest(socketIOClient, ackRequest, socketIOClient.getSessionId().toString(), removeTest);
            }
        });
    }

    /***
     * Handle removing a test logic
     *
     * @param client
     * @param ackRequest
     * @param sessionId
     * @param removeTest
     */
    private void removeTest(SocketIOClient client, AckRequest ackRequest, String sessionId, RemoveTest removeTest) {

        System.out.println(removeTest.getTestId());

        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        if(foundRedisUserStorage.isPresent()) {

            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            if(redisUserStorage.getRole().equals(Roles.PROFESSOR.toString())) {

                Optional<Test> foundTest = testsRepository.findById(removeTest.getTestId());

                if(foundTest.isPresent()) {

                    Test test = foundTest.get();

                    if(test.getTeacher().getTeacherId().equals(redisUserStorage.getTeacher().getTeacherId())) {

                        testsRepository.delete(test);

                        TestDAO testDAO = new TestDAO();

                        testDAO.setTestId(test.getTestId());

                        client.sendEvent("TEST_REMOVE", testDAO);

                    } else {
                        client.sendEvent("UNAUTHORIZED");
                    }

                } else {
                    client.sendEvent("BAD_REQUEST");
                }

            } else {
                client.sendEvent("UNAUTHORIZED");
            }

        } else {
            client.disconnect();
        }

    }

    /***
     * Handle marking test logic
     *
     * @param client
     * @param ackRequest
     * @param sessionId
     * @param markTest
     */
    private void markTest(SocketIOClient client, AckRequest ackRequest, String sessionId, MarkTest markTest) {

        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        if(foundRedisUserStorage.isPresent()) {

            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            if(redisUserStorage.getRole().equals(Roles.PROFESSOR.toString())) {

                Date date = new Date();

                if(markTest.getDate().compareTo(date) > 0) {

                    if(classesRepository.existsById(markTest.getClassId()) &&
                       teachersRepository.existsById(markTest.getTeacherId()) &&
                       disciplinesRepository.existsById(markTest.getDisciplineId())) {

                        Test newTest = new Test();

                        newTest.setDate(markTest.getDate());
                        newTest.setDiscipline(disciplinesRepository.getOne(markTest.getDisciplineId()));
                        newTest.setTeacher(teachersRepository.getOne(markTest.getTeacherId()));
                        newTest.setTestClass(classesRepository.getOne(markTest.getClassId()));

                        Test savedTest = testsRepository.save(newTest);

                        Optional<Test> foundTest = testsRepository.findById(savedTest.getTestId());

                        Test markedTest = foundTest.get();

                        TestDAO testDAO = new TestDAO();

                        testDAO.setTestId(markedTest.getTestId());
                        testDAO.setTeacherName(markedTest.getTeacher().getName());
                        testDAO.setTeacherId(markedTest.getTeacher().getTeacherId());
                        testDAO.setDiscipline(markedTest.getDiscipline().getName());
                        testDAO.setDate(markedTest.getDate());
                        testDAO.setClassName(markedTest.getTestClass().getName());
                        testDAO.setClassId(markedTest.getTestClass().getClassId());

                        client.sendEvent("MARK_TEST", testDAO);

                    } else {
                        client.sendEvent("INVALID_REQUEST");
                    }

                } else {
                    client.sendEvent("INVALID_DATE");
                }

            } else {
                client.sendEvent("UNAUTHORIZED");
            }

        } else {
            client.disconnect();
        }
    }

    /***
     * Handle gettings tests logic
     *
     * @param client
     * @param ackRequest
     * @param sessionId
     */
    private void getTests(SocketIOClient client, AckRequest ackRequest, String sessionId) {

        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        if(foundRedisUserStorage.isPresent()) {

            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            List<Test> userTests = getTestsByRole(redisUserStorage);

            if(userTests != null) {

                List<TestDAO> testDAOList = populateDTAList(userTests);

                client.sendEvent("GET_TESTS", testDAOList);
            } else {
                client.sendEvent("TESTS_NOT_FOUND");
            }


        } else {
            client.disconnect();
        }
    }

    /***
     * Helper to get all tests related to a Student
     *
     * @param redisUserStorage
     * @return
     */
    private List<Test> getTestsToStudent(RedisUserStorage redisUserStorage) {

        if(redisUserStorage.getUserClass() != null) {

            List<Test> studentTests = getTestsByClass(redisUserStorage.getUserClass());

            return studentTests;

        } else {

            Class studentClass = getClassByStudent(redisUserStorage.getStudent());

            if(studentClass != null) {

                redisUserStorage.setUserClass(studentClass);

                redisUsersStorageRepository.save(redisUserStorage);

                List<Test> studentTests = getTestsByClass(studentClass);

                if(studentTests != null) {
                    return studentTests;
                }
            }
        }

        return null;
    }

    /***
     * Helper to get all Tests depending on the User Role
     *
     * @param redisUserStorage
     * @return
     */
    private List<Test> getTestsByRole(RedisUserStorage redisUserStorage) {

        if(redisUserStorage.getRole().equals(Roles.ALUNO.toString())) {

            List<Test> studentTests = getTestsToStudent(redisUserStorage);

            if(studentTests != null) {

                return studentTests;
            }

        } else if(redisUserStorage.getRole().equals(Roles.PROFESSOR.toString())) {

            List<Test> teacherTests = getTestsByTeacher(redisUserStorage.getTeacher());

            if(teacherTests != null) {
                return teacherTests;
            }
        }

        return null;
    }

    private List<Test> getTestsByClass(Class userClass) {

        Date date = new Date();

        Optional<List<Test>> foundTests = testsRepository.findAllByTestClassAndDateAfter(userClass, date);

        if(foundTests.isPresent()) {
            return foundTests.get();
        }

        return null;
    }

    /***
     * Helper to get all Tests related to a Teacher
     *
     * @param teacher
     * @return
     */
    private List<Test> getTestsByTeacher(Teacher teacher) {

        Date date = new Date();

        Optional<List<Test>> foundTests = testsRepository.findAllByTeacherAndDateAfter(teacher, date);

        if(foundTests.isPresent()) {

            return foundTests.get();
        }

        return null;
    }

    /***
     * Helper to get Student Class
     *
     * @param student
     * @return
     */
    private Class getClassByStudent(Student student) {

        Optional<Class> foundClass = classesRepository.findFirstByStudents(student);

        if(foundClass.isPresent()) {
            return foundClass.get();
        }

        return null;
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

            Test test = tests.get(i);
            TestDAO testDAO = new TestDAO();

            testDAO.setClassId(test.getTestClass().getClassId());
            testDAO.setClassName(test.getTestClass().getName());
            testDAO.setDate(test.getDate());
            testDAO.setDiscipline(test.getDiscipline().getName());
            testDAO.setTeacherId(test.getTeacher().getTeacherId());
            testDAO.setTeacherName(test.getTeacher().getName());
            testDAO.setTestId(test.getTestId());

            testDAOList.add(testDAO);
        }

        return testDAOList;
    }
}
