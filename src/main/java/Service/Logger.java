package Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Logger {
    private String dir;
    private String fileName;

    private final Calendar calendar = new GregorianCalendar();

    // конструктор
    public Logger(String dir, String fileName) {
        this.dir = dir;
        this.fileName = fileName;
    }
    // геттеры-сеттеры
    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    // добавить в лог
    public void log(String text) {
        File logFile = new File(dir, fileName);
        try(FileWriter fw = new FileWriter(logFile, true);
            BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(text + "\n");
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }
    // добавить в лог
    public void logWithTime(String text) {
        log(calendar.getTime() + ": " + text);
    }
}
