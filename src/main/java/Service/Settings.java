package Service;

import java.io.*;
// Настройки
public class Settings implements Serializable {

    private int port;
    private String host;
    private String logPath;
    private String logFile;

    // геттеры-сеттеры
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }
    // toString
    @Override
    public String toString() {
        return "Service.Settings:\n" +
                "port = " + port +
                "\nhost = '" + host + '\'' +
                "\nlogPath = '" + logPath + '\'' +
                "\nlogFile = '" + logFile + '\'' ;
    }
    // сохранить настройки в файл
    public boolean save(String dir, String fileName) {
        File newDir = new File(dir);
        if(!newDir.exists()) {
            newDir.mkdir();
        }
        File file = new File(dir, fileName);
        try(FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this);
        } catch (Exception exp) {
            exp.printStackTrace();
            return false;
        }
        return true;
    }
    // получить настройки из файла
    public static Settings load(String dir, String fileName) {
        Settings settings;
        File file = new File(dir, fileName);
        if(!file.exists()) return null;
        try(FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            settings = (Settings) ois.readObject();
        } catch (Exception exp) {
            exp.printStackTrace();
            return null;
        }
        return settings;
    }
}
