package TestClientServer;

import Constants.MsgType;
import Server.Server;
import Service.Settings;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.Socket;

public class TestClientServer {
    final String DIR = "C://JAVA//NET_CHAT//TEST//ClientServer";
    final String SETTINGS_FILE = "settings.txt";
    final String SERVER_LOG_FILE = "log_server.txt";
    final String HOST = "127.0.0.1";
    static int PORT = 30000;
    final Settings settings = new Settings();
    Server server = new Server();

    public class TestClientClass implements Runnable{
        String userName;
        boolean stop;
        String result;

        public TestClientClass(String userName) {
            this.userName = userName;
            this.stop = false;
        }

        public void stop() {
            stop = true;
        }

        @Override
        public void run() {
            try(Socket socket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())))
            {
                out.println(userName);
                result = in.readLine();
                while(!stop);
                out.println("/exit");
            } catch(Exception exp) {
                exp.printStackTrace();
            }
        }
    }

    @BeforeEach
    public void initServer() {
        PORT = PORT + 1;
        settings.setPort(PORT);
        settings.setHost(HOST);
        settings.setLogPath(DIR);
        settings.setLogFile(SERVER_LOG_FILE);
        settings.save(DIR, SETTINGS_FILE);

        server.getSettings(DIR, SETTINGS_FILE);
        new Thread(null, server).start();
    }

    @AfterEach
    public void stop() {
        server.stop();
    }

    @Test
    public void Test_Client_Enter_Chat() throws InterruptedException{
        // given
        String name = "user1";
        TestClientClass client = new TestClientClass(name);
        String expect = name + " " + MsgType.CHAT_ENTER;
        // when
        new Thread(null, client).start();
        Thread.sleep(2000);
        client.stop();
        // then
        Assertions.assertEquals(expect, client.result);

    }
    @Test
    public void Test_Client_Enter_Chat_Name_Exist() throws InterruptedException{
        // given
        String name = "user2";
        TestClientClass client1 = new TestClientClass(name);
        TestClientClass client2 = new TestClientClass(name);
        String expect = MsgType.USERNAME_EXISTS.toString();
        // when
        new Thread(null, client1).start();
        Thread.sleep(2000);
        new Thread(null, client2).start();
        Thread.sleep(2000);
        client1.stop();
        client2.stop();
        // then
        Assertions.assertEquals(expect, client2.result);

    }


}
