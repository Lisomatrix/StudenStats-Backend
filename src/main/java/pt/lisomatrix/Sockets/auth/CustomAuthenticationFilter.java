package pt.lisomatrix.Sockets.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.redis.models.AuthenticatedUser;
import pt.lisomatrix.Sockets.redis.repositories.RedisAuthenticatedUsersRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomAuthenticationFilter extends GenericFilterBean {

    private static final String HEADER_STRING = "Authorization";
    private static final String OPTIONS_METHOD_STRING = "OPTIONS";

    private final RedisAuthenticatedUsersRepository redisAuthenticatedUsersRepository;

    public CustomAuthenticationFilter(RedisAuthenticatedUsersRepository redisAuthenticatedUsersRepository) {
        this.redisAuthenticatedUsersRepository = redisAuthenticatedUsersRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String method = ((HttpServletRequest) servletRequest).getMethod();

        if(method.equals(OPTIONS_METHOD_STRING)) {

            List<GrantedAuthority> authorities = new ArrayList<>();

            authorities.add(new SimpleGrantedAuthority(Roles.ALUNO.toString()));
            authorities.add(new SimpleGrantedAuthority(Roles.PARENTE.toString()));
            authorities.add(new SimpleGrantedAuthority(Roles.PROFESSOR.toString()));
            authorities.add(new SimpleGrantedAuthority(Roles.ADMIN.toString()));

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("123", null, authorities));
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String token = ((HttpServletRequest) servletRequest).getHeader(HEADER_STRING);

        if(token != null) {
            token = token.replace("Bearer", "");
        }

        if(token == null || token.trim().equals("")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Optional<AuthenticatedUser> foundAuthenticatedUser = redisAuthenticatedUsersRepository.findById(token);

        if(foundAuthenticatedUser.isPresent()) {

            AuthenticatedUser authenticatedUser = foundAuthenticatedUser.get();

            List<GrantedAuthority> authorities = new ArrayList<>();

            authorities.add(new SimpleGrantedAuthority(authenticatedUser.getRole()));

            Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser.getUserId(), null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
