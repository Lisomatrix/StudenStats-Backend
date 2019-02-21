package pt.lisomatrix.Sockets.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pt.lisomatrix.Sockets.config.UserDetailsImpl;
import pt.lisomatrix.Sockets.models.User;
import pt.lisomatrix.Sockets.repositories.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {

    private UsersRepository usersRepository;

    public UserDetailsServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Optional<User> foundUser = usersRepository.findFirstByUsername(s);

        if(foundUser.isPresent()) {

            User user = foundUser.get();

            UserDetailsImpl userDetails = new UserDetailsImpl();

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_PROFESSOR"));

            userDetails.setUsername(user.getUsername());
            userDetails.setPassword(user.getPassword());
            userDetails.setAuthorities(authorities);

            return userDetails;
        }

        throw new UsernameNotFoundException(s);
    }
}
