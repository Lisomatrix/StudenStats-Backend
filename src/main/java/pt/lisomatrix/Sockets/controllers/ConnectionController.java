package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;
import pt.lisomatrix.Sockets.redis.repositories.RedisUsersStorageRepository;
import pt.lisomatrix.Sockets.util.SessionHandler;

@Controller
public class ConnectionController {

    @Autowired
    private RedisUsersStorageRepository redisUsersStorageRepository;

    @Autowired
    private SessionHandler sessionHandler;

    @MessageMapping("/connect")
    @SendToUser("/queue/reply")
    public void setStorage(StompHeaderAccessor accessor) {


    }
}
