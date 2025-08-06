package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Objects;

public class Connection {
    public String username;
    public Session session;

    public Connection(String username, Session session) {
        this.username = username;
        this.session = session;
    }

    public void send(String message) throws IOException {
        session.getRemote().sendString(message);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Connection that = (Connection) o;
        return Objects.equals(username, that.username) && Objects.equals(session, that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, session);
    }
}
