package Server;

import Constants.MsgType;
import Service.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

// Соединение
public class Connection extends Thread{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Logger logger;
    private String userName;
    // список всех соединений(пользователей чата)
    //private static ConcurrentSkipListMap<String, Server.Connection> connections = new ConcurrentSkipListMap<>();
    private static ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    private final Calendar calendar = new GregorianCalendar();
    // конструктор
    public Connection(Socket socket, Logger logger) {
        this.socket = socket;
        this.logger = logger;

    }

    // инициализация
    public void init(String userName, BufferedReader in, PrintWriter out) {
        this.userName = userName;
        this.in = in;
        this.out = out;
    }

    // получить список соединений
    public static Collection<Connection> getConnections() {
        return connections.values();
    }

    // закрыть соединение
    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
            // удалить соединение из списка
            connections.remove(userName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // логирование
    private void log(String text) { logger.logWithTime(text);}

    // отправка сообщения всех пользователям списка
    private void sendMsgToAll(String msg) {
        // цикл по ключам
        for(String i : connections.keySet()) {
            // сокет
            Connection userConnection = connections.get(i);
            // отправка сообщения
            userConnection.out.println(msg);
        }
    }

    // преобразовать сообщение
    private String formatMsg(String text) {
        return calendar.getTime() + " <" + userName + ">: " + text;
    }

    // проверка ника пользователя на уникальность
    public static boolean checkName(String name) {
        return connections.containsKey(name);
    }

    // получить количество пользователей
    public static int getUsersCount() {
        return connections.size();
    }

    // run
    @Override
    public void run() {
        // добавить новое соединений в список
        connections.put(userName, this);
        String msg;
        // отправка сообщения всем
        msg = userName + " " + MsgType.CHAT_ENTER;
        sendMsgToAll(msg);
        // логирование
        log(msg);
        log("UsersCount = "+getUsersCount());
        /*
        // получение ника пользователя
        try {
            msg = in.readLine();
            // имя соединения(ник)
            userName = msg;
            // отправка сообщения всем
            msg = userName + " зашел в чат";
            sendMsgToAll(msg);
            // логирование
            log(msg);
        } catch (IOException exp) {
            log(exp.getMessage());
        }

         */
        // цикл чтения сообщений пользователя
        while (true) {
            try {
                // сообщение
                msg = in.readLine();
                // Выход если от клиента получили /exit
                if (msg.equals("/exit")) {
                    // удалить пользователя
                    connections.remove(userName);
                    // сообщение другим пользователям
                    sendMsgToAll(userName + " " + MsgType.CHAT_EXIT);
                    // отправить признак остановки
                    out.println("/stop");
                    // выход
                    break;
                }
                // преобразовать сообщение
                msg = formatMsg(msg);
                // запись в лог
                log(msg);
                // отправка сообщения всем пользователям
                sendMsgToAll(msg);
            } catch(IOException exp) {
                log(exp.getMessage());
            }
        }
    }
}
