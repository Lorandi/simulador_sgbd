package service;

import java.time.Instant;

public class FailService {
    public static void fail() {
        try {
            for (int i = 0; i < 1; i++) {
                Thread.sleep(500);
                System.out.println("\n     *****  FALHA  *****    \n");
            }
            RecoveryService.clearListUndoAndRedo();
            LogsService.clearLogsBuffer();
            DatabaseService.databaseClear();
            TransactionService.clearTransaction();

            var fail = "fail,,,,,," + Utils.formatDateTime(Instant.now().getEpochSecond());
            LogsService.addToLogDatabase(fail);
            LogsService.saveLogsOnLogsFile();

            Thread.sleep(500);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
