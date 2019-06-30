package bg.sofia.uni.fmi.mjt.project.splitwise.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Domain {
    private PrintWriter writer;
    private Socket socket;
    private boolean connected;

    public Domain(Socket socket) {
        this.socket = socket;
        this.writer = null;
        this.connected = false;
    }

    public void connect(String[] tokens) {
        setStream();
        writer.println(String.join(" ", tokens));
        turnOnListenerThread();
        connected = true;
    }

    private void setStream() {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error with open writer " + e.getMessage());
        }
    }

    private void turnOnListenerThread() {
        ClientRunnable clientRunnable = new ClientRunnable(socket);
        Thread newListener = new Thread(clientRunnable);
        newListener.start();
    }


}
