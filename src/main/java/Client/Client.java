package Client;

import Constants.*;
import Service.*;
import Service.Settings;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client implements Runnable {
    private int port;
    private String host;
    private Logger logger;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean stop;

    // конструктор
    public Client() {
        stop = false;
    }

    // геттеры
    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    // установить настройки
    public void getSettings(String dir, String file) {
        Settings settings = Settings.load(dir, file);
        if(settings!=null) {
            this.port = settings.getPort();
            this.host = settings.getHost();
            this.logger = new Logger(settings.getLogPath(), settings.getLogFile());
        }
    }

    // проверить параметры клиента
    private boolean checkClientParams() {
        return host != null && logger != null && port != 0;
    }

    // добавить в лог
    private void log(String text) {
        logger.logWithTime(text);
    }

    // прочитать сообщение от сервера
    private void readMsg() {
        try
        {
            String msg;
            while (!stop) {
                // прочитать сообщение
                msg = in.readLine();
                // проверка на выход
                if("/stop".equals(msg)) break;
                // вывод сообщения в консоль
                System.out.println(msg);
                // логирование
                log(msg);
            }
        } catch (IOException exp) {
            log(exp.getMessage());
            exp.printStackTrace();
        }
    }

    // закрытие всех closable
    private void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException exp) {
            log(exp.getMessage());
            exp.printStackTrace();
        }
    }

    // клиент
    @Override
    public void run() {
        if(checkClientParams()) {
            try (Scanner scanner = new Scanner(System.in)) {
                // подключение к серверу
                socket = new Socket(host, port);
                // потоки ввода-вывода
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // запуск потока на чтение сообщений от сервера
                new Thread(null, this::readMsg).start();
                // ник клиента для чата
                String msg;
                System.out.print(MsgType.USERNAME_ENTER);
                msg = scanner.nextLine();
                out.println(msg);
                // цикл отправки сообщений на сервер
                while (true) {
                    // сообщение
                    msg = scanner.nextLine();
                    // логирование
                    log("Вы: " + msg);
                    // отправка
                    out.println(msg);
                    // проверка на выход
                    if ("/exit".equals(msg)) break;
                }
                // отключение потока чтения сообщений
                stop = true;
            } catch (IOException exp) {
                log(exp.getMessage());
                exp.printStackTrace();
            } finally {
                close();
            }
        } else System.out.println(MsgType.CLIENT_RUN_ERROR);
    }
/*
    public static void main(String[] args) {
        Settings settings = new Settings();
        settings.setPort(Const.PORT);
        settings.setHost(Const.HOST);
        settings.setLogPath(Const.DIR);
        settings.setLogFile(Const.CLIENT_LOG_FILE);
        settings.save(Const.DIR, Const.CLIENT_SETTINGS_FILE);

        Client client = new Client();
        // получить настройки из файла-настроек
        client.getSettings(Const.DIR, Const.CLIENT_SETTINGS_FILE);
        new Thread(null, client).start();

    }
*/

}
