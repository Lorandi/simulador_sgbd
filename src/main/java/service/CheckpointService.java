package service;

import java.time.Instant;

public class CheckpointService {
    /** protocolo WAL (registro em log de Write-Ahead Logging))
     * escrita antecipada no log, ou seja, qualquer mudan¸ca em um objeto do banco de dados ´e primeiro gravada no log
     */
    public static void checkPoint() {
        LogsService.saveLogsOnLogsFile();
        DatabaseService.saveProductsFromBufferToDatabase();
        var check = "checkpoint,,,,,,"+ Utils.formatDateTime(Instant.now().getEpochSecond());
        LogsService.addToLogBuffer(check);
        LogsService.saveLogsOnLogsFile();
        System.out.println("\nCheckpoint realizado: " + check.substring(16));
    }
}
