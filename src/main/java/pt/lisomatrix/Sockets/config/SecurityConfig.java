package pt.lisomatrix.Sockets.config;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/***
 * Configure the security for the server endpoints
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /***
     * HTTP requests configurations
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Allow any requests to those URLs
        http.authorizeRequests()
                .antMatchers("/auth", "/register").permitAll().and().csrf().disable();
    }

}
