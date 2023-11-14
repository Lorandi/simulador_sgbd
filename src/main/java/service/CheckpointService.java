package service;

import entities.Database;

import java.time.Instant;

public class CheckpointService {
    /** protocolo WAL (registro em log de Write-Ahead Logging))
     * escrita antecipada no log, ou seja, qualquer mudan¸ca em um objeto do banco de dados ´e primeiro gravada no log
     */
    public static void checkPoint() {
        LogsService.logsFromDatabase();
        LogsService.saveLogsOnDatabase();
        Database.getInstance().saveOnFile();
        var check = "checkpoint,,,,,,"+ Utils.formatDateTime(Instant.now().getEpochSecond());
        LogsService.persistLogBuffer(check);
        LogsService.saveLogsOnDatabase();
        System.out.println("\nCheckpoint realizado: " + check.substring(16));
    }
}
