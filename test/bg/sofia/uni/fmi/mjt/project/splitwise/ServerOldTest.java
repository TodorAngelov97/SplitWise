package bg.sofia.uni.fmi.mjt.project.splitwise;

import java.net.Socket;

import org.junit.Before;
import org.mockito.Mock;

import bg.sofia.uni.fmi.mjt.project.splitwise.client.Client;

public class ServerOldTest {
	
	@Mock
	private Socket clientSocket;

	private Client client;

	@Before
	public void setUp() {
		client = new Client(clientSocket);
	}
}
