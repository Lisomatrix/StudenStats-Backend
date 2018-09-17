package pt.lisomatrix.Sockets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.lisomatrix.Sockets.util.RandomCodeGenerator;

/***
 * Helpers to generate random codes and hash password
 */
@Configuration
public class PasswordEncoderConfig {

    /***
     * Returns a PasswordEncoder instance
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /***
     * Returns a RandomCodeGenerator instance
     * @return RandomCodeGenerator
     */
    @Bean
    public RandomCodeGenerator randomCodeGenerator() {
        return new RandomCodeGenerator();
    }
}
