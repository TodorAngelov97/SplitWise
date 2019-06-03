package bg.sofia.uni.fmi.mjt.project.splitwise.server;

import java.net.Socket;

public class Domain {
    private Server server;
    private String username;
    private Socket socket;

    public Domain(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.username = null;
    }

    public Server getServer() {
        return server;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
