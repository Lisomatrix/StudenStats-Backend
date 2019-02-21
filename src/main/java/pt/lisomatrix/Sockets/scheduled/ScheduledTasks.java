package pt.lisomatrix.Sockets.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/***
 * This Class takes care of run certain task after some time
 *
 */
@Component
public class ScheduledTasks {

    @Scheduled(fixedRate = 10000)
    public void test() {
        System.out.println(java.lang.Thread.activeCount());
    }

}
