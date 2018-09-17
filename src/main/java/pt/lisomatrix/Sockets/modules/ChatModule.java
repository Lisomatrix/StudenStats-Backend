package pt.lisomatrix.Sockets.modules;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.protocol.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.lisomatrix.Sockets.models.ChatDTO;
import pt.lisomatrix.Sockets.models.Token;
import pt.lisomatrix.Sockets.repositories.TokensRepository;

@Component
public class ChatModule {

    private final SocketIONamespace namespace;

    @Autowired
    private TokensRepository tokenRepository;

    public ChatModule(SocketIOServer server) {
        this.namespace = server.addNamespace("/chat");
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());
        this.namespace.addEventListener("chat", ChatDTO.class, onChatReceived());
    }

    private DataListener<ChatDTO> onChatReceived() {
        return (client, data, ackSender) -> {
            System.out.println("Message received " + data.getUserName() + " " + data.getMessage());
            namespace.getBroadcastOperations().sendEvent("chat", data);
            tokenRepository.save(new Token(data.getMessage()));

        };
    }

    private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            System.out.println(client.getSessionId().toString());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            System.out.println("User disconnected");
        };
    }
}
