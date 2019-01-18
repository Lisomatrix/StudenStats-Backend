package pt.lisomatrix.Sockets.util;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionHandler {

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    public Boolean checkSameIP(String sessionId, String ip) {
        return sessionMap.get(sessionId).getRemoteAddress().getAddress().toString().replace("/", "").equals(ip);
    }

    public void register(WebSocketSession session) {
        sessionMap.put(session.getId(), session);
    }

    public void disconnectClient(String sessionId) {

        try {
            sessionMap.get(sessionId).close();
            sessionMap.remove(sessionId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAttribute(String sessionId, String name, Object data) {
        sessionMap.get(sessionId).getAttributes().put(name, data);
    }

    public Object getAttribute(String sessionId, String name) {
        return sessionMap.get(sessionId).getAttributes().get(name);
    }

}
