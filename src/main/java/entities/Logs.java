package entities;

import java.util.ArrayList;
import java.util.List;

public class Logs {

    private static List<String> logsBuffer = new ArrayList<>();
    private static List<String> logsDatabase = new ArrayList<>();

    public static List<String> getLogsBuffer() {
        return logsBuffer;
    }

    public static List<String> getLogsDatabase() { return logsDatabase; }

    public static void setLogsBuffer(List<String> list) { logsBuffer = list; }

    public static void setLogsDatabase(List<String> list) { logsDatabase = list;}

    public static void clearLogsBuffer() { logsBuffer.clear(); }

    public static void clearLogsDatabase() { logsDatabase.clear(); }

    public static void addLogBuffer(String log) { logsBuffer.add(log); }

    public static void addLogDatabase(String log) { logsDatabase.add(log);}

    public static void removeLogBuffer(String log) {
        logsBuffer.remove(log);
    }

    public static void removeLogDatabase(String log) {
        logsDatabase.remove(log);
    }
}