package pt.lisomatrix.Sockets.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/***
 * Configure the security for the server endpoints
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
        /*http.authorizeRequests()
                .antMatchers("/auth", "/register").permitAll().and().csrf().disable();*/

        http.authorizeRequests().anyRequest().permitAll()
                .and().csrf().disable().cors().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }



}
