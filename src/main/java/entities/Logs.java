package entities;

import java.util.ArrayList;
import java.util.List;

import static service.LogsService.logsFromDatabase;

public class Logs {
    private static final Logs logs = new Logs();
    private static final List<String> logsBuffer = new ArrayList<>();
    private static final List<String> logsDatabase = new ArrayList<>();

    public static Logs getInstance() {
        return logs;
    }

    public List<String> getLogsBuffer() {
        return logsBuffer;
    }

    public List<String> getLogsDatabase() {
        return logsDatabase;
    }

    public void persistLogBuffer(String log) {
        logsBuffer.add(log);
    }

    public void removeLogBuffer(String log) {
        logsBuffer.remove(log);
    }

    public void persistLogDatabase(String log) {
        logsDatabase.add(log);
    }

    public void removeLogDatabase(String log) {
        logsDatabase.remove(log);
    }
}