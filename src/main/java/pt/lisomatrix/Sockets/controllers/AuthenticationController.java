package pt.lisomatrix.Sockets.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.RedirectView;
import pt.lisomatrix.Sockets.constants.Roles;
import pt.lisomatrix.Sockets.models.*;
import pt.lisomatrix.Sockets.redis.models.RedisToken;
import pt.lisomatrix.Sockets.repositories.*;
import pt.lisomatrix.Sockets.redis.repositories.RedisTokenRepository;
import pt.lisomatrix.Sockets.requests.models.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/***
 * Takes care of authenticating and registering users
 */
@RestController
public class AuthenticationController {

    /***
     * Database Repository to save tokens
     */
    @Autowired
    private TokensRepository tokensRepository;

    /***
     * Database Repository to get and create users
     */
    @Autowired
    private UsersRepository usersRepository;

    /***
     * Password Encoder helper
     */
    @Autowired
    private PasswordEncoder passwordEncoder;


    /***
     * Redis Repository to save tokens
     */
    @Autowired
    private RedisTokenRepository redisTokenRepository;

    /***
     * PasswordResets Repository to save and update
     */
    @Autowired
    private PasswordResetsRepository passwordResetsRepository;

    /***
     * Java Email Sender to send reset codes
     */
    @Autowired
    private JavaMailSender javaMailSender;

    /***
     * Database Repository to get and create students
     */
    @Autowired
    private StudentsRepository studentsRepository;

    /***
     * Database Repository to get and create teachers
     */
    @Autowired
    private TeachersRepository teachersRepository;

    /***
     * Database Repository to get and create parents
     */
    @Autowired
    private ParentsRepository parentsRepository;

    /***
     * Database Repository to get and create permanent Tokens
     */
    @Autowired
    private PermanentTokensRepository permanentTokensRepository;

    /***
     * Takes care of the reset validation and password renovation
     *
     * @param passwordRecovery
     * @return ResponseEntity<?>
     */
    @PostMapping("/reset")
    @CrossOrigin
    public ResponseEntity<?> resetUserPassword(@RequestBody PasswordRecovery passwordRecovery) {

        // Get PasswordReset
        Optional<PasswordReset> foundPasswordReset = passwordResetsRepository.findById(passwordRecovery.getResetId());

        // If found
        if(foundPasswordReset.isPresent()) {

            // Get PasswordReset
            PasswordReset passwordReset = foundPasswordReset.get();

           if(!passwordReset.isUsed()) {

               // If reset code matches with the given one
               if(passwordReset.getResetCode() == passwordRecovery.getResetCode()) {

                   // Renew the password
                   passwordReset.getUser().setPassword(passwordEncoder.encode(passwordRecovery.getPassword()));

                   // Update the database
                   usersRepository.save(passwordReset.getUser());

                   // Set used
                   passwordReset.setUsed(true);

                   // Update the database
                   passwordResetsRepository.save(passwordReset);

                   // Generate Response
                   Response successResponse = generateResponse("Senha renovada", true);

                   // Send Response
                   return new ResponseEntity<>(successResponse, HttpStatus.OK);

               } else {

                   // Generate Response
                   Response errorResponse = generateResponse("Código de recuperação inválido", false);

                   // Send Response
                   return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
               }

           } else {

               // Generate Response
               Response errorResponse = generateResponse("Código de recuperação inválido", false);

               // Send Response
               return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
           }

        } else {
            // Generate Response
            Response errorResponse = generateResponse("Identificador de recuperação inválido", false);

            // Send Response
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }
    }

    /***
     * Takes care of saving and sending a reset email to the user email with a reset code
     *
     * @param userEmail
     * @return ResponseEntity<?>
     */
    @GetMapping("/reset/{userEmail}")
    @CrossOrigin
    public ResponseEntity<?> requestResetUserPassword(@PathVariable @NotNull String userEmail) {

        // Get User with email
        Optional<User> foundUser = usersRepository.findByEmail(userEmail.toLowerCase());

        // If found
        if(foundUser.isPresent()) {

            // Get user
            User user = foundUser.get();
            // Generate unique id
            String uniqueId = UUID.randomUUID().toString().replace("-", "");
            // Generate random reset code
            int resetCode = generateRandomCode();

            // Create PasswordReset and populate it
            PasswordReset passwordReset = new PasswordReset();

            passwordReset.setId(uniqueId);
            passwordReset.setUser(user);
            passwordReset.setResetCode(resetCode);
            passwordReset.setUsed(false);

            // Save password reset to database
            passwordResetsRepository.save(passwordReset);

            // Create email and populate it
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setSubject("Recuperar Senha");
            mailMessage.setTo(userEmail);
            mailMessage.setText("Use este código para recuperar a sua senha: " + resetCode);

            // Send email
            javaMailSender.send(mailMessage);

            // Generate response
            Response successResponse = generateResponse(uniqueId.replace("-", ""), true);

            // Send response
            return new ResponseEntity<>(successResponse, HttpStatus.OK);

        } else {
            // Generate response
            Response errorResponse = generateResponse("Email inválido", false);

            // Send response
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }
    }

    /***
     * Takes care of authenticating the user and save a token on redis and on database
     *
     * @param authentication // The required object with the authentication credentials
     * @return ResponseEntity<?>
     */
    @CrossOrigin
    @PostMapping("/auth")
    public ResponseEntity<?> userAuthentication(@RequestBody Authentication authentication, HttpServletRequest request) {

        // Get request ip address
        String ipAddress = request.getRemoteAddr();

        // Get the user with the email
        Optional<User> foundUser = this.usersRepository.findByEmail(authentication.getEmail().toLowerCase());

        // If found
        if(foundUser.isPresent()) {

            // Retrieve it
            User user = foundUser.get();

            // Check if password matches
            if(this.passwordEncoder.matches(authentication.getPassword(), user.getPassword())) {

                RedisToken redisToken = generateAccessToken(user, ipAddress);

                if(authentication.getRemember()) {
                    UUID token = UUID.randomUUID();

                    PermanentToken permanentToken = new PermanentToken();

                    permanentToken.setPermanentToken(token.toString().replace("-", ""));
                    permanentToken.setUser(user);

                    permanentTokensRepository.save(permanentToken);

                    RememberToken rememberToken = new RememberToken();

                    rememberToken.setRedisToken(redisToken);
                    rememberToken.setPermanentToken(permanentToken.getPermanentToken());

                    return new ResponseEntity<>(rememberToken, HttpStatus.OK);
                } else {
                    // Return Token
                    return new ResponseEntity<>(redisToken, HttpStatus.OK);
                }


            }  else {

                // Generate error response
                Response errorResponse = generateResponse("Senha errada", false);

                // Return Response
                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
            }
        } else {
            // Generate error response
            Response errorResponse = generateResponse("Credenciais inválidos", false);

            // Return Response
            return new ResponseEntity<>(errorResponse ,HttpStatus.FORBIDDEN);
        }
    }

    /***
     * Takes care of authenticating the user and save a token on redis and on database
     *
     * @param token // The required string with the remember me token
     * @return ResponseEntity<?>
     */
    @CrossOrigin
    @PostMapping("/auth/token")
    public ResponseEntity<?> tokenAuthentication(@RequestBody TokenAuthentication token, HttpServletRequest request) {

        Optional<PermanentToken> foundPermanentToken = permanentTokensRepository.findFirstByPermanentToken(token.getToken());

        if(foundPermanentToken.isPresent()) {

            PermanentToken permanentToken = foundPermanentToken.get();

            User user = permanentToken.getUser();

            // Get request ip address
            String ipAddress = request.getRemoteAddr();

            RedisToken redisToken = generateAccessToken(user, ipAddress);

            permanentToken.setPermanentToken(UUID.randomUUID().toString().replace("-", ""));

            permanentTokensRepository.save(permanentToken);

            RememberToken rememberToken = new RememberToken();

            rememberToken.setPermanentToken(permanentToken.getPermanentToken());
            rememberToken.setRedisToken(redisToken);

            // Return Token
            return new ResponseEntity<>(rememberToken, HttpStatus.OK);

        } else {
            // Generate error response
            Response errorResponse = generateResponse("Credenciais inválidos", false);

            // Return Response
            return new ResponseEntity<>(errorResponse ,HttpStatus.FORBIDDEN);
        }
    }

    /***
     * Takes care of registering a user with the given registration code
     *
     * @param registration // Required credentials to register
     * @return ResponseEntity<?>
     */
    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<?> userRegistration(@RequestBody Registration registration) {

       // Get User with the registration code
       Optional<User> foundUser = usersRepository.findByRegistrationCode(registration.getRegistrationCode());

       // Check if User exists
       if(foundUser.isPresent()) {
           // Check if given email already exists
           Optional<User> emailExists = usersRepository.findByEmail(registration.getEmail().toLowerCase());

           if(!emailExists.isPresent()) {

               // Get user
               User user = foundUser.get();

               if(!user.getCreated()) {
                   // Populate fields
                   user.setEmail(registration.getEmail().toLowerCase());
                   user.setPassword(passwordEncoder.encode(registration.getPassword()));
                   user.setCreated(true);

                   // Update the database
                   usersRepository.save(user);

                   // Generate response
                   Response successResponse = generateResponse("Conta criada", true);

                   // Send response
                   return new ResponseEntity<>(successResponse, HttpStatus.OK);
               } else {

                   // Generate response
                   Response errorResponse = generateResponse("Conta já criada", false);

                   // Send response
                   return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
               }

           } else {
               // Generate error response
               Response errorResponse = generateResponse("Email já existente", false);

               // Send response
               return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
           }

       } else {
           // Generate error response
           Response errorResponse = generateResponse("Código de registo inválido", false);

           // Send response
           return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
       }
   }

    @RequestMapping("/swagger-ui")
    public String redirectToUi() {
        return "redirect:/";
    }

    /***
     * Response Generate helper
     *
     * @param message // Message to send on response
     * @param success // Success message to send
     * @return Response
     */
    private Response generateResponse(Object message, boolean success) {
        // Create response and populate it
        Response response = new Response();

        response.setSuccess(success);
        response.setMessage(message);

        //return response
        return response;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public RedirectView notFound() {
        return new RedirectView("/");
    }

    /***
     * Generates a random 5 digit integer
     *
     * @return int
     */
    private int generateRandomCode() {
        Random r = new Random( System.currentTimeMillis() );
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }

    private RedisToken generateAccessToken(User user, String ipAddress) {

        // Get current date
        Date currentDate = new Date();
        // Generate unique id
        UUID uniqueId = UUID.randomUUID();

        // Create token and populate it
        Token accessToken = new Token();

        accessToken.setToken(uniqueId.toString().replace("-", ""));
        accessToken.setUserId(user.getUserId());
        accessToken.setDate(currentDate);

        // Save token to redis and database
        RedisToken redisToken = new RedisToken();

        redisToken.setToken(accessToken.getToken());
        redisToken.setUsed(false);
        redisToken.setRole(user.getRole().getRole());
        redisToken.setUserId(user.getUserId());

        redisToken.setIpAddress(ipAddress);

        if(user.getRole().getRole().equals(Roles.ALUNO.toString())) {

            Optional<Student> foundStudent = studentsRepository.findFirstByUser(user);

            if(foundStudent.isPresent()) {
                redisToken.setUserRoleId(foundStudent.get().getStudentId());
            }

        } else if(user.getRole().getRole().equals(Roles.PROFESSOR.toString())) {

            Optional<Teacher> foundTeacher = teachersRepository.findFirstByUser(user);

            if(foundTeacher.isPresent()) {
                redisToken.setUserRoleId(foundTeacher.get().getTeacherId());
            }

        } else if (user.getRole().getRole().equals(Roles.PARENTE.toString())) {

            Optional<Parent> foundParent = parentsRepository.findFirstByUser(user);

            if(foundParent.isPresent()) {
                redisToken.setUserRoleId(foundParent.get().getParentId());
            }
        }

        tokensRepository.save(accessToken);
        redisTokenRepository.save(redisToken);

        // Remove non necessary info
        redisToken.setUsed(null);
        redisToken.setIpAddress(null);

        return redisToken;
    }
}
