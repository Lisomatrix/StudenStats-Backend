package pt.lisomatrix.Sockets.modules;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.models.UserSettings;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.repositories.UserSettingsRepository;
import pt.lisomatrix.Sockets.websocket.models.ThemeDAO;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Listens and handles all WebSocket messages related to User Settings
 *
 */
@Component
public class SettingsModule {

    /***
     * WebSockets server object
     */
    private SocketIOServer server;

    /***
     * User Settings Repository to save and update
     */
    private UserSettingsRepository userSettingsRepository;

    /***
     * REDIS Users Storage repository to get in ram info
     */
    private RedisUsersStorageRepository redisUsersStorageRepository;

    /***
     * Initialize all class dependencies and
     * Start listening to specific WebSocket messages
     *
     * @param server
     * @param userSettingsRepository
     * @param redisUsersStorageRepository
     */
    @Autowired
    public SettingsModule(SocketIOServer server, UserSettingsRepository userSettingsRepository, RedisUsersStorageRepository redisUsersStorageRepository) {
        this.server = server;
        this.userSettingsRepository = userSettingsRepository;
        this.redisUsersStorageRepository = redisUsersStorageRepository;

        // Listen for the following events
        this.server.addEventListener("UPDATE_THEME", ThemeDAO.class, new DataListener<ThemeDAO>() {
            @Override
            public void onData(SocketIOClient socketIOClient, ThemeDAO themeDAO, AckRequest ackRequest) throws Exception {
                updateTheme(socketIOClient, themeDAO, ackRequest, socketIOClient.getSessionId().toString());
            }
        });

        this.server.addEventListener("GET_THEME", ThemeDAO.class, new DataListener<ThemeDAO>() {
            @Override
            public void onData(SocketIOClient socketIOClient, ThemeDAO themeDAO, AckRequest ackRequest) throws Exception {
                getTheme(socketIOClient, ackRequest, socketIOClient.getSessionId().toString());
            }
        });
    }

    /***
     * Handle getting theme logic
     *
     * @param client
     * @param ackRequest
     * @param sessionId
     */
    private void getTheme(SocketIOClient client, AckRequest ackRequest, String sessionId) {

        // Get user redis storage
        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        // Check if exists
        if(foundRedisUserStorage.isPresent()) {

            // Get user redis storage
            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            // Check if User Settings exists
            Optional<UserSettings> foundUserSettings = userSettingsRepository.findFirstByUser(redisUserStorage.getUser());

            if(foundRedisUserStorage.isPresent()) {

                // If so get it
                UserSettings userSettings = foundUserSettings.get();

                // Send current theme
                client.sendEvent("GET_THEME", userSettings.getTheme());
            }

        } else {
            client.disconnect();
        }
    }

    /***
     * Handle updating user theme logic
     *
     * @param client
     * @param theme
     * @param ackRequest
     * @param sessionId
     */
    private void updateTheme(SocketIOClient client, ThemeDAO theme, AckRequest ackRequest, String sessionId) {

        //theme.setPrimaryColor("#ff1818");
        client.sendEvent("test", theme);

        // Get user redis storage
        Optional<RedisUserStorage> foundRedisUserStorage = redisUsersStorageRepository.findById(sessionId);

        // Check if exists
        if(foundRedisUserStorage.isPresent()) {

            // Get user redis storage
            RedisUserStorage redisUserStorage = foundRedisUserStorage.get();

            // Check if User Settings exists
            Optional<UserSettings> foundUserSettings = userSettingsRepository.findFirstByUser(redisUserStorage.getUser());

            if(foundRedisUserStorage.isPresent()) {

                // If so get it
                UserSettings userSettings = foundUserSettings.get();

                ThemeDAO currentTheme = new ThemeDAO();

                // Populate theme with the current theme
                currentTheme.populate(userSettings.getTheme());

                // Validate each color
                // If valid then set them
                // Otherwise leave old color
                if(validateColor(theme.getBackgroundColor())) {
                    currentTheme.setBackgroundColor(theme.getBackgroundColor());
                }

                if(validateColor(theme.getHeaderColor())) {
                    currentTheme.setHeaderColor(theme.getHeaderColor());
                }

                if(validateColor(theme.getPrimaryColor())) {
                    currentTheme.setPrimaryColor(theme.getPrimaryColor());
                }

                if(validateColor(theme.getSecondaryColor())) {
                    currentTheme.setSecondaryColor(theme.getSecondaryColor());
                }

                if(validateColor(theme.getTextPrimaryColor())) {
                    currentTheme.setTextPrimaryColor(theme.getTextPrimaryColor());
                }

                if(validateColor(theme.getTextSecondaryColor())) {
                    currentTheme.setTextSecondaryColor(theme.getTextSecondaryColor());
                }

                if(validateColor(theme.getCardBackground())) {
                    currentTheme.setCardBackground(theme.getCardBackground());
                }

                if(validateColor(theme.getIconColor())) {
                    currentTheme.setIconColor(theme.getIconColor());
                }

                if(validateColor(theme.getButtonColor())) {
                    currentTheme.setButtonColor(theme.getButtonColor());
                }
                
                currentTheme.setDark(theme.getDark());

                // Set theme
                userSettings.setTheme(currentTheme.toJSON());

                // Update settings
                userSettingsRepository.save(userSettings);

                // Set Redis theme configuration
                redisUserStorage.setUserSettings(userSettings);

                // Update settings on REDIS
                redisUsersStorageRepository.save(redisUserStorage);

                client.sendEvent("THEME_UPDATE", currentTheme);

            } else {
                client.sendEvent("ERROR");
            }

        } else {
            client.disconnect();
        }

    }

    /***
     * Helper to validate given color
     *
     * @param color
     * @return
     */
    private boolean validateColor(String color) {
        if(color == null) {
            return false;
        }

        Pattern colorPattern = Pattern.compile("#([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8})");
        Matcher m = colorPattern.matcher(color);

        return m.matches();
    }
}
