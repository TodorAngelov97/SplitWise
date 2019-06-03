package bg.sofia.uni.fmi.mjt.project.splitwise.server;

public class Domain {
    private Server server;
    private String username;

    public Domain(Server server) {
        this.server = server;
        this.username = null;
    }

    public Server getServer() {
        return server;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
