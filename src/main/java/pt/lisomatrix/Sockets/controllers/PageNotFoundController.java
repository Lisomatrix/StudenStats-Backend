package pt.lisomatrix.Sockets.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestController
public class PageNotFoundController {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleError404() {
        return "redirect:/index.html";
    }

    @GetMapping("/testing")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public String testing() {
        return "WORKING";
    }
}
