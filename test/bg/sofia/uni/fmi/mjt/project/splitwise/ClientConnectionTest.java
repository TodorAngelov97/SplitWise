package bg.sofia.uni.fmi.mjt.project.splitwise;

import bg.sofia.uni.fmi.mjt.project.splitwise.server.ClientConnection;
import bg.sofia.uni.fmi.mjt.project.splitwise.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class ClientConnectionTest {

    private static final String USER_KIRO_NAME = "kiro";
    private static final String USER_TODOR_NAME = "todor";
    private Server server;
    private static final String TEST_DATA_PATH = "resources/testData.json";
    private static final String ORIGINAL_PATH = "resources/originalTestData.json";

    private ServerSocket serverSocket;

    @Before
    public void setUp() throws IOException {
        serverSocket = new ServerSocket(8080);
        server = new Server(serverSocket, TEST_DATA_PATH);
    }

    @Test
    public void testSignUpUser() throws IOException {
        Socket socket = Mockito.mock(Socket.class);
        int initial = server.getNumberOfUsers();
        String inputData = "sign-up kiro kiro kiro kiro kiro \n" + "logout";
        InputStream stream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Mockito.when(socket.getInputStream()).thenReturn(stream);
        Mockito.when(socket.getOutputStream()).thenReturn(System.out);

        ClientConnection clientConnection = new ClientConnection(socket, server);
        clientConnection.run();
        assertEquals(initial + 1, server.getNumberOfUsers());
    }

    @Test
    public void testLoginUser() throws IOException {
        Socket socket = Mockito.mock(Socket.class);
        int initial = server.getNumberOfActiveUsers();
        System.out.println(initial);
        String inputData = "login todor todor\n" + "logout";
        InputStream stream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Mockito.when(socket.getInputStream()).thenReturn(stream);
        Mockito.when(socket.getOutputStream()).thenReturn(System.out);

        ClientConnection clientConnection = new ClientConnection(socket, server);
        clientConnection.run();
        assertEquals(initial, server.getNumberOfActiveUsers());
    }

    @Test
    public void testAddFriend() throws IOException {
        Socket socket = Mockito.mock(Socket.class);
        int initial = server.getNumberOfFriends(USER_TODOR_NAME);
        String inputData = "sign-up kiro kiro kiro kiro kiro \n" + "add-friend todor \n" + "logout";
        InputStream stream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Mockito.when(socket.getInputStream()).thenReturn(stream);
        Mockito.when(socket.getOutputStream()).thenReturn(System.out);

        ClientConnection clientConnection = new ClientConnection(socket, server);
        clientConnection.run();
        assertEquals(initial + 1, server.getNumberOfFriends(USER_TODOR_NAME));
    }

    @Test
    public void testSplitMoney() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final String inputData = "sign-up kiro kiro kiro kiro kiro \n" + "add-friend todor \n"
                + "split 15 todor hapvane\n" + "logout";
        InputStream stream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Mockito.when(socket.getInputStream()).thenReturn(stream);
        Mockito.when(socket.getOutputStream()).thenReturn(System.out);

        ClientConnection clientConnection = new ClientConnection(socket, server);
        clientConnection.run();
        double owedSum = server.getFriendAmount(USER_TODOR_NAME, "kiro");
        assertEquals(-7.5, owedSum, 0.01);
    }

    @Test
    public void testPayedMoney() throws IOException {
        Socket socket = Mockito.mock(Socket.class);
        String inputData = "sign-up kiro kiro kiro kiro kiro \n" + "add-friend todor \n" + "payed 15 todor\n"
                + "logout";
        InputStream stream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Mockito.when(socket.getInputStream()).thenReturn(stream);
        Mockito.when(socket.getOutputStream()).thenReturn(System.out);

        ClientConnection clientConnection = new ClientConnection(socket, server);
        clientConnection.run();
        double owedSum = server.getFriendAmount(USER_TODOR_NAME, USER_KIRO_NAME);
        System.out.println(owedSum);
        assertEquals(15, owedSum, 0.01);
    }

    @Test
    public void testCreateGroup() throws IOException {
        Socket socketForFirstFriend = Mockito.mock(Socket.class);
        String inputDataForFirstFriend = "sign-up kiro kiro kiro kiro kiro \n" + "add-friend todor \n" + "logout";
        InputStream streamForFirstFriend = new ByteArrayInputStream(
                inputDataForFirstFriend.getBytes(StandardCharsets.UTF_8));
        Mockito.when(socketForFirstFriend.getInputStream()).thenReturn(streamForFirstFriend);
        Mockito.when(socketForFirstFriend.getOutputStream()).thenReturn(System.out);

        ClientConnection firstClient = new ClientConnection(socketForFirstFriend, server);
        firstClient.run();

        Socket socketForSecondFriend = Mockito.mock(Socket.class);
        String inputDataForSecondFriend = "sign-up vanko vanko vanko vanko vanko \n" + "add-friend todor\n"
                + "add-friend kiro\n" + "logout";
        InputStream streamForSecondFriend = new ByteArrayInputStream(
                inputDataForSecondFriend.getBytes(StandardCharsets.UTF_8));
        Mockito.when(socketForSecondFriend.getInputStream()).thenReturn(streamForSecondFriend);
        Mockito.when(socketForSecondFriend.getOutputStream()).thenReturn(System.out);

        ClientConnection secondClient = new ClientConnection(socketForSecondFriend, server);
        secondClient.run();

        Socket socketForThirdFriend = Mockito.mock(Socket.class);
        String inputDataForThirdFriend = "login todor todor\n" + "create-group tigri vanko kiro\n" + "logout";
        InputStream streamForThirdFriend = new ByteArrayInputStream(
                inputDataForThirdFriend.getBytes(StandardCharsets.UTF_8));
        Mockito.when(socketForThirdFriend.getInputStream()).thenReturn(streamForThirdFriend);
        Mockito.when(socketForThirdFriend.getOutputStream()).thenReturn(System.out);

        ClientConnection thirdClient = new ClientConnection(socketForThirdFriend, server);
        thirdClient.run();

        int numberOfGroups = server.getGroups(USER_TODOR_NAME).size();
        assertEquals(1, numberOfGroups);
    }

    @After
    public void cleanUp() throws IOException {

        File file = new File(TEST_DATA_PATH);
        PrintWriter writer = new PrintWriter(file);

        FileReader fileReader = new FileReader(ORIGINAL_PATH);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        while ((line = reader.readLine()) != null) {
            writer.print(line);
        }
        writer.close();
        reader.close();
        serverSocket.close();
    }


}
