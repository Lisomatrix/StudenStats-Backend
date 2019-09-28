package pt.lisomatrix.Sockets.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.Hour;
import pt.lisomatrix.Sockets.models.Schedule;
import pt.lisomatrix.Sockets.models.ScheduleDay;
import pt.lisomatrix.Sockets.repositories.*;
import pt.lisomatrix.Sockets.requests.models.ScheduleData;
import pt.lisomatrix.Sockets.response.models.ScheduleResponse;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
public class SchedulesRestController {

    private SchedulesRepository schedulesRepository;

    private ScheduleHoursRepository scheduleHoursRepository;

    private ScheduleExceptionsRepository scheduleExceptionsRepository;

    private HoursRepository hoursRepository;

    private ScheduleDayRepository scheduleDayRepository;

    private List<Hour> hours;

    private List<ScheduleDay> scheduleDays;

    public SchedulesRestController(SchedulesRepository schedulesRepository, ScheduleHoursRepository scheduleHoursRepository, 
        ScheduleExceptionsRepository scheduleExceptionsRepository, ScheduleDayRepository scheduleDayRepository, HoursRepository hoursRepository) {

        this.scheduleDayRepository = scheduleDayRepository;
        this.scheduleHoursRepository = scheduleHoursRepository;
        this.scheduleExceptionsRepository = scheduleExceptionsRepository;
        this.schedulesRepository = schedulesRepository;
        this.hoursRepository = hoursRepository;

        hours = hoursRepository.findAll();
        scheduleDays = scheduleDayRepository.findAll();
    }

    @GetMapping("/schedule")
    @CrossOrigin
    public ResponseEntity<ScheduleData> getScheduleData() {
        return ResponseEntity.ok(new ScheduleData(hours, scheduleDays));
    }

    @GetMapping("/class/{classId}/schedule")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasRole('ROLE_ALUNO')")
    @CrossOrigin
    public ScheduleResponse getClassSchedule(@PathVariable("classId") long classId) {

        Optional<Schedule> schedule = schedulesRepository.findFirstByClass(classId);

        if(schedule.isPresent()) {
            ScheduleResponse scheduleResponse = new ScheduleResponse();

            scheduleResponse.populate(schedule.get());

            return scheduleResponse;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Schedule not found");
    }

    @GetMapping("/class/{classId}/schedule/async")
    @PreAuthorize("hasRole('ROLE_PROFESSOR') or hasRole('ROLE_ALUNO')")
    @CrossOrigin
    public DeferredResult<ResponseEntity<?>> getClassScheduleAsync(@PathVariable("classId") long classId) {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        CompletableFuture.supplyAsync(() -> schedulesRepository.findFirstByClass(classId))
                .thenApply(foundSchedule -> {
                    if(foundSchedule.isPresent()) {

                        ScheduleResponse scheduleResponse = new ScheduleResponse();

                        scheduleResponse.populate(foundSchedule.get());;

                        deferredResult.setResult(ResponseEntity.ok(scheduleResponse));

                    } else {
                        deferredResult.setResult(ResponseEntity.notFound().build());
                    }

                    return null;
                });

        return deferredResult;
    }

    @GetMapping("/teacher/{teacherId}/schedule")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public ScheduleResponse getTeacherSchedule(@PathVariable("teacherId") long teacherId) {

        Optional<Schedule> schedule = schedulesRepository.findFirstByTeacher(teacherId);

        if(schedule.isPresent()) {
            ScheduleResponse scheduleResponse = new ScheduleResponse();

            scheduleResponse.populate(schedule.get());

            return scheduleResponse;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Schedule not found");
    }

    @GetMapping("/teacher/{teacherId}/schedule/async")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @CrossOrigin
    public DeferredResult<ResponseEntity<?>> getTeacberScheduleAsync(@PathVariable("teacherId") long teacherId) {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        CompletableFuture.supplyAsync(() -> schedulesRepository.findFirstByTeacher(teacherId))
                .thenApply(foundSchedule -> {
                    if(foundSchedule.isPresent()) {

                        ScheduleResponse scheduleResponse = new ScheduleResponse();

                        scheduleResponse.populate(foundSchedule.get());

                        deferredResult.setResult(ResponseEntity.ok(scheduleResponse));
                    } else {
                        deferredResult.setResult(ResponseEntity.notFound().build());
                    }

                    return null;
                });

        return deferredResult;
    }
}
