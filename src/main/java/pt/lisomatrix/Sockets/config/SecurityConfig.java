package pt.lisomatrix.Sockets.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pt.lisomatrix.Sockets.auth.*;
import pt.lisomatrix.Sockets.redis.repositories.RedisAuthenticatedUsersRepository;
import pt.lisomatrix.Sockets.repositories.UsersRepository;

import java.util.Arrays;
import java.util.concurrent.Executor;

/***
 * Configure the security for the server endpoints
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RedisAuthenticatedUsersRepository redisAuthenticatedUsersRepository;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("ASYNC_HTTP");
        executor.initialize();

        return executor;
    }

    /***
     * HTTP requests configurations
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll().and().csrf().disable();
        http
                .requestCache().requestCache(new NullRequestCache()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable().cors().and().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login", "/auth", "/auth/token", "/register", "/reset").permitAll()
                .antMatchers(HttpMethod.GET, "/test/async", "/test/sync", "/actuator/**", "/absence", "/reset/**", "/", "/static/**", "/color.less", "/favicon.ico", "/icons/**", "/service-worker.js").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new CustomAuthenticationFilter(redisAuthenticatedUsersRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CorsFilter(), CustomAuthenticationFilter.class);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/css/**", "/static/js/**");
        super.configure(web);
    }
}
