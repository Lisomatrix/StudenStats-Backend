package pt.lisomatrix.Sockets.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.repositories.LessonsRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/***
 * This Class takes care of run certain task after some time
 *
 */
@Component
public class ScheduledTasks {

    /***
     * REDIS Users Storage repository to get in ram info
     * Created by dependency injection
     *
     */
    @Autowired
    private RedisUsersStorageRepository redisUsersStorageRepository;

    /***
     * Cleans all Redis Users Storage that are inactive for more than 1 hour
     * Runs every 3600000 milliseconds (1 Hour)
     * TODO SHOULD PROBABLY INCREASE
     */
    @Scheduled(fixedRate = 3600000)
    public void deleteUsersStorage() {

        // Get disconnected redis user storages
        List<RedisUserStorage> redisUserStorage = (List<RedisUserStorage>) redisUsersStorageRepository.findAll();

        // Check if present
        if(redisUserStorage.size() > 0) {

            // Get current date
            Date actualDate = new Date();

            // Iterate them
            for(int i = 0; i < redisUserStorage.size(); i++) {

                // Get element
                RedisUserStorage temp = redisUserStorage.get(i);

                // Check if is disconnected
                if(temp.getDisconnected()) {

                    DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                    Date date;

                    try {

                        // Get date
                        date = format.parse(temp.getDate());

                        // Calculate difference
                        long difference = getDateDiff(date, actualDate, TimeUnit.SECONDS);
                        //long difference = getDateDiff(temp.getDate(), actualDate, TimeUnit.HOURS);

                        // If 1 hour passed
                        if(difference >= 1) {

                            // Delete from redis
                            redisUsersStorageRepository.delete(temp);
                        }

                    } catch (Exception e) {

                    }
                }
            }
        }
    }


    /**
     * Helper to get the difference between 2 dates
     * Thank you Stack Overflow
     *
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the difference
     * @return the difference value, in the provided unit
     */
    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();

        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

}
