import Client.Client;
import Constants.Const;
import Server.Server;
import Service.Settings;

import java.util.Scanner;

public class Main {
    // инициализация настроек
    private static void initSettings(boolean serverMode) {
        Settings settings = new Settings();
        settings.setPort(Const.PORT);
        settings.setHost(Const.HOST);
        settings.setLogPath(Const.DIR);
        if(serverMode) {
            settings.setLogFile(Const.SERVER_LOG_FILE);
            settings.save(Const.DIR, Const.SETTINGS_FILE);
        }
        else {
            settings.setLogFile(Const.CLIENT_LOG_FILE);
            settings.save(Const.DIR, Const.CLIENT_SETTINGS_FILE);
        }
    }
    // запуск в режиме сервера
    public static void startServer() {
        // инициализация настроек
        initSettings(true);
        // сервер
        Server server = new Server();
        // получить настройки из файла-настроек
        server.getSettings(Const.DIR, Const.SETTINGS_FILE);
        // поток сервера
        Thread serverThread = new Thread(null, server);
        serverThread.start();
    }
    // запуск в режиме клиента
    public static void startClient() {
        // инициализация настроек
        initSettings(false);
        Client client = new Client();
        // получить настройки из файла-настроек
        client.getSettings(Const.DIR, Const.CLIENT_SETTINGS_FILE);
        new Thread(null, client).start();
    }

    public static void main(String[] args) {
        String input;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Запустить программу в режиме сервера(y) или клиента(n), (end) для выхода:");
        while (true) {
            input = scanner.nextLine();
            // проверка на выход
            if("end".equals(input)) {
                break;
            }
            // запуск в режиме сервера
            if("y".equals(input)) {
                startServer();
                break;
            }
            // запуск в режиме клиента
            if("n".equals(input)) {
                startClient();
                break;
            }
            System.out.println("Неверно введено значение!");
        }

    }
}
