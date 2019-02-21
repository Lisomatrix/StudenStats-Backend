package pt.lisomatrix.Sockets.auth;


import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader("Access-Control-Allow-Origin"  , "*"                               );
        response.setHeader("Access-Control-Allow-Methods" , "POST, PUT, GET, OPTIONS, DELETE" );
        response.setHeader("Access-Control-Allow-Headers" , "Authorization, Content-Type"     );
        response.setHeader("Access-Control-Max-Age"       , "3600"                            );

        if("OPTIONS".equalsIgnoreCase(((HttpServletRequest) servletRequest).getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
            chain.doFilter(servletRequest, servletResponse);
        }
    }
}
