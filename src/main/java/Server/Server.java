package Server;

import Constants.MsgType;
import Service.Logger;
import Service.Settings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Collection;
import java.util.List;

public class Server implements Runnable {
    private int port;
    private Logger logger;
    private boolean stop = false;
    private Settings settings;

    // получить порт
    public int getPort() {
        return port;
    }

    public boolean isStop() {
        return stop;
    }

    // установить настройки
    public void getSettings(String dir, String file) {
        // получить настройки из файла
        settings = Settings.load(dir, file);
        // настройки есть
        if(settings!=null) {
            this.port = settings.getPort();
            this.logger = new Logger(settings.getLogPath(), settings.getLogFile());
        }
    }
    // добавить в лог
    private void log(String text) {
        logger.logWithTime(text);
    }
    // проверить параметры сервера
    private boolean checkServerParams() {
        return settings != null && logger != null && port != 0;
    }
    // сервер
    @Override
    public void run() {
        if(checkServerParams()) {
            stop = false;
            log("Запуск сервера...");
            while (!stop) {
                // остановка сервера
                if (Thread.interrupted()) {
                    // закрытие соединений
                    closeConnections();
                    break;
                }
                try (ServerSocket serverSocket = new ServerSocket(port)) {
                    Socket clientSocket = null;
                    PrintWriter out = null;
                    BufferedReader in = null;
                    String name;
                    try {
                        // ожидание нового подключение
                        clientSocket = serverSocket.accept();
                        log(clientSocket.toString());
                        // открытие потоков ввода-вывода
                        out = new PrintWriter(clientSocket.getOutputStream(), true);
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        while (true) {
                            name = in.readLine();
                            log(name);
                            if (!Connection.checkName(name)) break;
                            out.println(MsgType.USERNAME_EXISTS);
                        }
                        // создание нового соединения
                        Connection connection = new Connection(clientSocket, logger);
                        connection.init(name, in, out);
                        // запуск потока на соединение
                        connection.start();
                        // логирование
                        log("new connection: " + clientSocket);

                    } catch (Exception exp) {
                        if (in != null) in.close();
                        if (out != null) out.close();
                        if (clientSocket != null) clientSocket.close();
                    }
                } catch (Exception exp) {
                    log(exp.getMessage());
                    exp.printStackTrace();
                    break;
                }
            }
            // закрыть соединения
            closeConnections();
            log("Работа сервера завершена");
        } else System.out.println(MsgType.SERVER_RUN_ERROR);
   }
   // закрыть соединения
   private void closeConnections() {
       Collection<Connection> connections = Connection.getConnections();
       // цикл по соединениям
       for(Connection userConnection : connections) {
           // закрытие соединения
           userConnection.close();
       }
       connections.clear();
   }
    // получить количество пользователей
    public int getUsersCount() {
        return Connection.getUsersCount();
    }
   // остановить сервер
   public void stop() {
        stop = true;
        log("Вызов остановки сервера");
   }

}
