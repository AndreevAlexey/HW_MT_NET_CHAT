package TestClient;

import Client.Client;
import Service.Settings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestClient {
    final String DIR = "C://JAVA//NET_CHAT//TEST";
    final String SETTINGS_FILE = "settingsClient.txt";
    final String CLIENT_LOG_FILE = "log_client.txt";
    final String HOST = "127.0.0.1";
    final int PORT = 30000;
    Settings settings = new Settings();
    Client client = new Client();

    @BeforeEach
    public void init() {
        settings.setPort(PORT);
        settings.setHost(HOST);
        settings.setLogPath(DIR);
        settings.setLogFile(CLIENT_LOG_FILE);
        settings.save(DIR, SETTINGS_FILE);

        client.getSettings(DIR, SETTINGS_FILE);
    }

    @Test
    public void Test_getSettings_Port() {
        //given
        int expected = 30000;
        //when
        int result = client.getPort();
        //then
        Assertions.assertEquals(expected, result);

    }

    @Test
    public void Test_getSettings_Host() {
        //given
        String expected = "127.0.0.1";
        //when
        String result = client.getHost();
        //then
        Assertions.assertEquals(expected, result);

    }
}
