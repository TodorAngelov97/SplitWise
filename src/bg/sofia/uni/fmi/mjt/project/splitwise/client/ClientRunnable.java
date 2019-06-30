package bg.sofia.uni.fmi.mjt.project.splitwise.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientRunnable implements Runnable {

    private Socket socket;

    public ClientRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            outputClientInformation(reader);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void outputClientInformation(BufferedReader reader) throws IOException {
        while (true) {
            if (socket.isClosed()) {
                String message = "Client socket is closed, stop waiting for server messages";
                System.out.println(message);
                return;
            }
            String nextLine = reader.readLine();
            System.out.println(nextLine);
        }
    }
}
