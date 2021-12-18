package TestServer;

import Server.Server;
import Service.Settings;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.Socket;

public class TestServer {
    final String DIR = "C://JAVA//NET_CHAT//TEST";
    final String SETTINGS_FILE = "settings.txt";
    final String SERVER_LOG_FILE = "log_server.txt";
    final String HOST = "127.0.0.1";
    final int PORT = 30000;
    final Settings settings = new Settings();
    Server server = new Server();

    @BeforeEach
    public void init() {
        settings.setPort(PORT);
        settings.setHost(HOST);
        settings.setLogPath(DIR);
        settings.setLogFile(SERVER_LOG_FILE);
        settings.save(DIR, SETTINGS_FILE);

        server.getSettings(DIR, SETTINGS_FILE);
    }

    @AfterEach
    public void stop() {
        server.stop();
    }

    @Test
    public void Test_getSettings_Port() {
        //given
        int expected_port = 30000;
        //when
        int result = server.getPort();
        //then
        Assertions.assertEquals(expected_port, result);

    }

    @Test
    public void Test_connect_to_server() {
        // given
        boolean expected = true;
        boolean result = false;
        String userName = "user1";
        Thread serverThread = new Thread(null, server);
        serverThread.start();
        // подключение к серверу
        try(Socket socket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            result = socket.isConnected();
            out.println(userName);
            out.println("/exit");
        } catch (IOException exp) {
            exp.printStackTrace();
        }
        // then
        Assertions.assertEquals(expected, result);
    }
}
