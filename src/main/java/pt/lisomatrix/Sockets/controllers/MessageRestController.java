package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.User;
import pt.lisomatrix.Sockets.repositories.UsersRepository;
import pt.lisomatrix.Sockets.requests.models.SendMessage;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class MessageRestController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @CrossOrigin
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    @PostMapping("/class/{classId}/message")
    public ResponseEntity<?> sendEmailMessage(@PathVariable("classId") Long classId, @RequestBody SendMessage sendMessage, Principal principal) {

        Optional<List<User>> foundParents = usersRepository.findAllByClassId(classId);

        if(foundParents.isPresent()) {

            List<User> users = foundParents.get();

            String[] emails = new String[users.size()];

            for(int i = 0; i < users.size(); i++) {
                emails[i] = users.get(i).getUsername();
            }

            sendEmailToParent(emails, sendMessage.getMessage(), sendMessage.getSubject());

            return ResponseEntity.ok().build();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Class not found");
    }

    @Async
    private void sendEmailToParent(String[] emails, String message, String subject) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setSubject(subject);
        mailMessage.setTo(emails);
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);
    }
}
