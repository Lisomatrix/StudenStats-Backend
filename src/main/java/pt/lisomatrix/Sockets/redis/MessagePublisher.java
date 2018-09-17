package pt.lisomatrix.Sockets.redis;

public interface MessagePublisher {

    void publish(final String message);
}
