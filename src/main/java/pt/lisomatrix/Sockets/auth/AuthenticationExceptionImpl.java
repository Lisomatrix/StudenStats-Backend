package pt.lisomatrix.Sockets.auth;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationExceptionImpl extends AuthenticationException {

    public AuthenticationExceptionImpl(String msg) {
        super(msg);
    }
}
