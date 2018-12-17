package pt.lisomatrix.Sockets.modules;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.Absence;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Discipline;
import pt.lisomatrix.Sockets.models.Lesson;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.repositories.ClassesRepository;
import pt.lisomatrix.Sockets.repositories.DisciplinesRepository;
import pt.lisomatrix.Sockets.repositories.LessonsRepository;
import pt.lisomatrix.Sockets.requests.models.GetClassLessons;
import pt.lisomatrix.Sockets.requests.models.NewLesson;
import pt.lisomatrix.Sockets.requests.models.UpdateLessonSummary;
import pt.lisomatrix.Sockets.websocket.models.LessonDAO;
import pt.lisomatrix.Sockets.websocket.models.StudentDAO;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

/***
 * Listens and handles all WebSocket messages related to lessons
 *
 */
@Component
public class LessonModule {

    /***
     * WebSockets server object
     */
    private SocketIOServer server;

    /***
     * Lessons Repository to save and update
     */
    private LessonsRepository lessonsRepository;

    /***
     * REDIS Users Storage repository to get in ram info
     */
    private RedisUsersStorageRepository redisUsersStorageRepository;

    /***
     * Classes Repository to get info
     */
    private ClassesRepository classesRepository;

    /***
     * Disciplines Repository to get info
     */
    private DisciplinesRepository disciplinesRepository;

    /***
     * Initialize all class dependencies and
     * Start listening to specific WebSocket messages
     *
     * @param server
     * @param lessonsRepository
     * @param classesRepository
     * @param redisUsersStorageRepository
     * @param disciplinesRepository
     */
    @Autowired
    public LessonModule(SocketIOServer server, LessonsRepository lessonsRepository, ClassesRepository classesRepository, RedisUsersStorageRepository redisUsersStorageRepository, DisciplinesRepository disciplinesRepository) {
        this.server = server;
        this.lessonsRepository = lessonsRepository;
        this.redisUsersStorageRepository = redisUsersStorageRepository;
        this.classesRepository = classesRepository;
        this.disciplinesRepository = disciplinesRepository;

        // Listen for the following events
        this.server.addEventListener("NEW_LESSON", NewLesson.class, new DataListener<NewLesson>() {
            @Override
            public void onData(SocketIOClient client, NewLesson newLesson, AckRequest ackRequest) throws Exception {
                newLesson(client, newLesson, ackRequest, client.getSessionId().toString());
            }
        });

        this.server.addEventListener("GET_LESSONS", GetClassLessons.class, new DataListener<GetClassLessons>() {
            @Override
            public void onData(SocketIOClient socketIOClient, GetClassLessons lesson, AckRequest ackRequest) throws Exception {
                getLessonsByRole(socketIOClient, lesson, ackRequest, socketIOClient.getSessionId().toString());
            }
        });

        this.server.addEventListener("UPDATE_LESSON_SUMMARY", UpdateLessonSummary.class, new DataListener<UpdateLessonSummary>() {
            @Override
            public void onData(SocketIOClient socketIOClient, UpdateLessonSummary updateLessonSummary, AckRequest ackRequest) throws Exception {
                updateLessonSummary(socketIOClient, updateLessonSummary, ackRequest, socketIOClient.getSessionId().toString());
            }
        });
    }

    /***
     * Handle updating lesson summary logic
     *
     * @param client
     * @param updateLessonSummary
     * @param ackRequest
     * @param sessionId
     */
    private void updateLessonSummary(SocketIOClient client, UpdateLessonSummary updateLessonSummary, AckRequest ackRequest, String sessionId) {

        // Get user redis storage
        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        // Check if exists
        if(foundRedisUserStorage.isPresent()) {

            // Get user redis storage
            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            // Check if is allowed
            if(redisUserStorage.getRole().equals(Roles.PROFESSOR.toString())) {

                lessonsRepository.updateSummary(updateLessonSummary.getSummary(), updateLessonSummary.getLessonId());

                client.sendEvent("SUMMARY_UPDATED");

            } else {
                client.sendEvent("UNAUTHORIZED");
            }

        } else {
            client.disconnect();
        }
    }

    /***
     * Handle creating a new lesson logic
     *
     * @param client
     * @param newLesson
     * @param ackRequest
     * @param sessionId
     */
    private void newLesson(SocketIOClient client, NewLesson newLesson, AckRequest ackRequest, String sessionId) {

        // Get user redis storage
        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        // Check if exists
        if(foundRedisUserStorage.isPresent()) {

            // Get user redis storage
            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            // Check if is allowed
            if(redisUserStorage.getRole().equals(Roles.PROFESSOR.toString())) {

                // Check if is valid and
                // if so create new lesson
                createNewLesson(client, newLesson, redisUserStorage);

            } else {
                client.sendEvent("UNAUTHORIZED");
            }

        } else {
            client.disconnect();
        }

    }

    /***
     * Handle getting lessons depending on the User Role
     *
     * @param client
     * @param getClassLessons
     * @param ackRequest
     * @param sessionId
     */
    private void getLessonsByRole(SocketIOClient client, GetClassLessons getClassLessons, AckRequest ackRequest, String sessionId) {

        // Get User redis storage
        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        // Check if exists
        if(foundRedisUserStorage.isPresent()) {

            // Get user redis storage
            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            // Check if is a student
            if(redisUserStorage.getRole().equals(Roles.ALUNO.toString())) {

                // Check if requested class is the same as student class
                if(getClassLessons.getClassId() == redisUserStorage.getUserClass().getClassId()) {

                    // Get requested discipline object
                    Discipline requestDiscipline = getDiscipline(redisUserStorage, getClassLessons.getDisciplineId());

                    // Check if found
                    if(requestDiscipline != null) {

                        // Get the requested lessons and send
                        List<Lesson> requestedLessons = getLessons(redisUserStorage.getUserClass(), requestDiscipline);

                        if(requestedLessons != null) {

                            List<LessonDAO> lessonDAOList = populateLessonsDAOList(requestedLessons, redisUserStorage.getUserClass().getClassId(), getClassLessons.getDisciplineId());

                            client.sendEvent("GET_LESSONS", lessonDAOList);
                        }
                    }

                } else {
                    client.sendEvent("BAD_REQUEST");
                }

                return;

            } else if(redisUserStorage.getRole().equals(Roles.PROFESSOR.toString())) {

                // Get the class from the request object
                Class requestClass = getClass(redisUserStorage, getClassLessons.getClassId());

                // Check if found
                if(requestClass != null) {

                    // Get the discipline from the request object
                    Discipline discipline = getDiscipline(redisUserStorage, getClassLessons.getDisciplineId());

                    // Check if found
                    if(discipline != null) {

                        // Get the requested lessons and send
                        List<Lesson> requestedLessons = getLessons(requestClass, discipline);

                        if(requestedLessons != null) {

                            List<LessonDAO> lessonDAOList = populateLessonsDAOList(requestedLessons, getClassLessons.getClassId(), getClassLessons.getDisciplineId());

                            client.sendEvent("GET_LESSONS", lessonDAOList);

                            return;
                        } else {

                            List<LessonDAO> lessonDAOList = new ArrayList<LessonDAO>();

                            client.sendEvent("GET_LESSONS", lessonDAOList);
                        }
                    }
                }
            }

            client.sendEvent("BAD_REQUEST");

        } else {
            client.disconnect();
        }
    }

    /***
     * Handle creating a new lesson validation logic
     *
     * @param client
     * @param newLesson
     * @param redisUserStorage
     */
    private void createNewLesson(SocketIOClient client, NewLesson newLesson, RedisUserStorage redisUserStorage) {

        // Check if request object is valid
        if(newLesson == null || !newLesson.getTeacherId().equals(redisUserStorage.getTeacher().getTeacherId())) {
            client.sendEvent("BAD_REQUEST");
            return;
        }

        // Get Class from redis user storage
        Class userClass = getClass(redisUserStorage, newLesson.getClassId());

        if(userClass != null) {

            // Get Discipline from redis
            Discipline discipline = getDiscipline(redisUserStorage, newLesson.getDisciplineId());

            if(discipline != null) {

                // Get current date
                Date currentDate = new Date();

                // Create database object and populate it
                Lesson lesson = new Lesson();

                lesson.setClasse(userClass);
                lesson.setDiscipline(discipline);
                lesson.setTeacher(redisUserStorage.getTeacher());
                lesson.setDate(currentDate);


                Optional<Lesson> foundLesson = lessonsRepository.findFirstByClasseAndDisciplineOrderByLessonNumberDesc(userClass, discipline);

                if(foundLesson.isPresent()) {

                    Lesson lastLesson = foundLesson.get();

                    lesson.setLessonNumber(lastLesson.getLessonNumber() + 1);
                    System.out.println(lastLesson.getLessonNumber() + 1);
                    // Insert object to database
                    Lesson createdLesson = lessonsRepository.save(lesson);

                    LessonDAO createdLessonDAO = new LessonDAO();

                    createdLessonDAO.setLessonId(createdLesson.getLessonId());
                    createdLessonDAO.setDisciplineId(discipline.getDisciplineId());
                    createdLessonDAO.setDate(createdLesson.getDate());
                    createdLessonDAO.setClassId(userClass.getClassId());
                    createdLessonDAO.setLessonNumber(createdLesson.getLessonNumber());

                    // Warn client
                    client.sendEvent("NEW_LESSON", createdLessonDAO);

                } else {

                    lesson.setLessonNumber(1);

                    // Insert object to database
                    Lesson createdLesson = lessonsRepository.save(lesson);

                    LessonDAO createdLessonDAO = new LessonDAO();

                    createdLessonDAO.setLessonId(createdLesson.getLessonId());
                    createdLessonDAO.setDisciplineId(discipline.getDisciplineId());
                    createdLessonDAO.setDate(createdLesson.getDate());
                    createdLessonDAO.setClassId(userClass.getClassId());
                    createdLessonDAO.setLessonNumber(createdLesson.getLessonNumber());

                    // Warn client
                    //client.sendEvent("NEW_LESSON");
                    client.sendEvent("NEW_LESSON", createdLessonDAO);
                }

                return;
            }
        }

        client.sendEvent("BAD_REQUEST");

    }

    /***
     * Helper to get all Lessons by User Class
     *
     * @param userClass
     * @return
     */
    private List<Lesson> getLessons(Class userClass) {

        Optional<List<Lesson>> foundLessons = lessonsRepository.findAllByClasse(userClass);

        if(foundLessons.isPresent()) {

            return foundLessons.get();
        }

        return null;
    }

    /***
     * Helper to get All Lessons by User Class and Discipline
     *
     * @param userClass
     * @param discipline
     * @return
     */
    private List<Lesson> getLessons(Class userClass, Discipline discipline) {

        Optional<List<Lesson>> foundLessons = lessonsRepository.findAllByClasseAndDiscipline(userClass, discipline);

        if(foundLessons.isPresent()) {

            return foundLessons.get();
        }

        return null;

    }

    /***
     * Helper to get User Class by classId
     *
     * @param redisUserStorage
     * @param classId
     * @return
     */
    private Class getClass(RedisUserStorage redisUserStorage, long classId) {

        List<Class> redisClasses = redisUserStorage.getClasses();

        for(int i = 0; i < redisClasses.size(); i++) {

            Class tempClass = redisClasses.get(i);

            if(tempClass.getClassId() == classId) {

                return tempClass;
            }
        }

        Optional<Class> foundClass = classesRepository.findById(classId);

        if(foundClass.isPresent()) {

            return foundClass.get();
        }


        return null;
    }

    /***
     * Helper to get Discipline by disciplineId
     *
     * @param redisUserStorage
     * @param disciplineId
     * @return
     */
    private Discipline getDiscipline(RedisUserStorage redisUserStorage, long disciplineId) {

        List<Discipline> redisDisciplines = redisUserStorage.getTeacherDisciplines();

        for(int i = 0; i < redisDisciplines.size(); i++) {

            Discipline tempDiscipline = redisDisciplines.get(i);

            if(tempDiscipline.getDisciplineId() == disciplineId) {

                return tempDiscipline;
            }
        }

        Optional<Discipline> foundDiscipline = disciplinesRepository.findById(disciplineId);

        if(foundDiscipline.isPresent()) {

            return foundDiscipline.get();
        }

        return null;
    }

    /***
     * Helper to convert from List<Lesson> to List<LessonDAO>
     *
     * @param lessons
     * @param classId
     * @param disciplineId
     * @return
     */
    private List<LessonDAO> populateLessonsDAOList(List<Lesson> lessons, long classId, long disciplineId) {

        List<LessonDAO> lessonDAOList = new ArrayList<LessonDAO>();

        for(int i = 0; i < lessons.size(); i++) {

            Lesson tempLesson = lessons.get(i);

            LessonDAO lessonDAO = new LessonDAO();

            lessonDAO.setClassId(classId);
            lessonDAO.setDate(tempLesson.getDate());
            lessonDAO.setDisciplineId(disciplineId);
            lessonDAO.setLessonId(tempLesson.getLessonId());
            lessonDAO.setLessonNumber(tempLesson.getLessonNumber());
            lessonDAO.setSummary(tempLesson.getSummary());

            lessonDAOList.add(lessonDAO);
        }

        return lessonDAOList;
    }

}
