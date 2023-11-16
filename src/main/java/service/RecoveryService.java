package service;

import entities.Product;
import entities.Recovery;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecoveryService {


    public static void addToListUndo(String undo) {
        Recovery.addToListUndo(undo);
    }

    public static void addToListRedo(String redo) {
        Recovery.addToListRedo(redo);
    }

    public static void clearListUndoAndRedo() {
        Recovery.removeAllListUndo();
        Recovery.removeAllListRedo();
    }

    public static List<String> getListUndo() {
        return Recovery.getListUndo();
    }

    public static List<String> getListRedo() {
        return Recovery.getListRedo();
    }

    public static void printInfos(String string, int time) throws InterruptedException {
        System.out.println();
        System.out.print(string);
        for (int i = 0; i < 10; i++) {
            System.out.print(".");
            Thread.sleep(time);
        }
        System.out.println();
    }

    public static void printList(String listType, List<String> list) {
        System.out.println(listType);
        if (!list.isEmpty()) {
            for (String st : list) {
                System.out.println(st);
            }
        } else {
            System.out.println(" - Lista vazia");
        }
        System.out.println();
    }

    public static void recoveryFromDatabaseLogs() {
        try {
            var recovery = "recovery,,,,,," + Utils.formatDateTime(Instant.now().getEpochSecond());
            LogsService.addToLogDatabase(recovery);
            LogsService.saveLogsOnLogsFile();

            printInfos("Recovery em andamento", 200);
            printInfos("Conexão com banco sendo reestabelecida", 200);
            System.out.println("Banco de dados OK");
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        recovery();
    }

    public static void checkingDatabaseIntegrity() {
        try {
            var restart = "restart,,,,,," + Utils.formatDateTime(Instant.now().getEpochSecond());
            LogsService.getLogsFromLogsFile();
            LogsService.addToLogDatabase(restart);
            LogsService.saveLogsOnLogsFile();

            printInfos("Verificando integridade do banco de dados", 200);
            System.out.println("Banco de dados OK");
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recovery();
    }


    public static void recovery() {

        DatabaseService.reloadProductsDatabaseAndBuffer();
        LogsService.reloadLogsDatabase();

        List<String> listUndo = getListUndo();
        List<String> listRedo = getListRedo();
        List<String> transactions = LogsService.getLogsDatabase();

        List<String> finalized = new ArrayList<>();
        List<String> finalizedRedo = new ArrayList<>();
        List<String> updateRedo = new ArrayList<>();
        List<String> updatedRedo = new ArrayList<>();
        List<String> updateUndo = new ArrayList<>();
        List<String> updatedUndo = new ArrayList<>();

        boolean checkpoint = false;

        for (int i = transactions.size(); i > 0; i--) {
            String[] temp = transactions.get(i - 1).split(",");

            if (temp[0].equals("checkpoint")) {
                checkpoint = true;
            }

            if (temp[0].equals("finaliza-redo")) {
                finalizedRedo.add(temp[1]);
                finalized.add(temp[1]);
            }

            if (temp[0].equals("updated-redo")) {
                updatedRedo.add(temp[1]);
            }

            if (temp[0].equals("updated-undo")) {
                updatedUndo.add(temp[1]);
            }

            if (temp[0].equals("finaliza") && !finalizedRedo.contains(temp[1])) {
                finalized.add(temp[1]);
                if (!checkpoint) {
                    listRedo.add(temp[1]);
                    finalizedRedo.add(temp[1]);
                    LogsService.addToLogDatabase("finaliza-redo," + temp[1] + ",,,,," + Utils.formatDateTime(Instant.now().getEpochSecond()));
                }
            }

            if (temp[0].equals("update") && finalizedRedo.contains(temp[1]) && !updatedRedo.contains(temp[1])) {
                updateRedo.add(transactions.get(i - 1));
            }

            if (!finalized.contains(temp[1]) && (temp[0].equals("inicia") || temp[0].equals("update")) && !updatedUndo.contains(temp[1])) {
                if (!listUndo.contains(temp[1])) addToListUndo(temp[1]);
            }

            if (temp[0].equals("update") && listUndo.contains(temp[1]) && !updatedUndo.contains(temp[1])) {
                updateUndo.add(transactions.get(i - 1));
            }
        }

        updateList("redo", updateRedo);
        updateList("undo", updateUndo);

        DatabaseService.saveProductsFromBufferToDatabase();
        LogsService.saveLogsOnLogsFile();

        System.out.println("\nReports de recovery");
        printList("Lista undo", listUndo);
        printList("Lista redo", listRedo);
    }

    public static void updateList(String listType, List<String> list) {
        if (!list.isEmpty()) {
            System.out.println("Executando " + listType + "...");

            boolean inverse = false;

            if (listType.equals("redo")) {
                Collections.reverse(list);
                inverse = true;
            }
            for (String st : list) {
                String[] temp = st.split(",");
                int quant = 4;
                if (inverse) quant = 5;
                System.out.println(st);

                int index = Integer.parseInt(temp[2]);
                Product product = DatabaseService.getProductBuffer(index);

                switch (temp[3]) {
                    case "quantidade":
                        product.setQuantity(Integer.parseInt(temp[quant]));
                        break;
                    case "preço":
                        product.setPrice(new BigDecimal(temp[quant]));
                        break;
                    case "nome":
                        product.setName(temp[quant]);
                        break;
                }
                DatabaseService.replaceProductBuffer(index, product);

                LogsService.addToLogDatabase("updated-" + listType + "," + temp[1] + ",,,,," + Utils.formatDateTime(Instant.now().getEpochSecond()));
            }
        }
    }
}
