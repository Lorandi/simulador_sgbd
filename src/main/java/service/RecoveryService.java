package service;

import entities.Database;
import entities.Logs;
import entities.Product;
import entities.Recovery;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static entities.Database.productsBuffer;
import static service.LogsService.logsFromDatabase;

public class RecoveryService {
    private static final Recovery recovery = Recovery.getInstance();
    private static final List<String> listUndo = recovery.getListUndo();
    private static final List<String> listRedo = recovery.getListRedo();

    public static void addToListUndo(String undo) {
        recovery.persistListUndo(undo);
    }

    public static void addToListRedo(String redo) {
        recovery.persistListRedo(redo);
    }

    public static void fail() {
        try {
            for (int i = 0; i < 1; i++) {
                Thread.sleep(500);
                System.out.println("\n     *****  FALHA  *****    \n");
            }
            Recovery.getInstance().removeAllListUndo();
            Recovery.getInstance().removeAllListRedo();
            LogsService.logsFromDatabase();
            LogsService.clearLogsBuffer();
            var fail = "fail,,,,,," + Utils.formatDateTime(Instant.now().getEpochSecond());
            LogsService.persistLogDatabase(fail);
            LogsService.saveLogsOnDatabase();
            Database.productBufferClear();
            TransactionService.clearTransaction();

            Thread.sleep(500);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void recoveryFromDatabaseLogs() {
        try {

            System.out.print("\nRecovery em andamento");
            for (int i = 0; i < 10; i++) {
                System.out.print(".");
                Thread.sleep(200);
            }
            System.out.println();

            Database.getInstance().productsBuffer(productsBuffer);
            System.out.print("\nConexão com banco sendo reestabelecida");
            for (int i = 0; i < 10; i++) {
                System.out.print(".");
                Thread.sleep(200);
            }

            System.out.println();


            List<String> transactions = Logs.getInstance().getLogsDatabase();

            List<String> ativas = new ArrayList<>();
            List<String> finalizadas = new ArrayList<>();
            List<String> finalizadasRedo = new ArrayList<>();
            List<String> updateRedo = new ArrayList<>();
            List<String> updatedRedo = new ArrayList<>();
            List<String> updateUndo = new ArrayList<>();
            List<String> updatedUndo = new ArrayList<>();


            if (transactions.isEmpty()) {
                logsFromDatabase();
            }

            boolean checkpoint = false;

            for (int i = transactions.size(); i > 0; i--) {
                String[] temp = transactions.get(i - 1).split(",");

                if (temp[0].equals("checkpoint")) {
                    checkpoint = true;
                }

                if (temp[0].equals("finaliza-redo")) {
                    finalizadasRedo.add(temp[1]);
                    finalizadas.add(temp[1]);
                }

                if (temp[0].equals("updated-redo")) {
                    updatedRedo.add(temp[1]);
                }

                if (temp[0].equals("updated-undo")) {
                    updatedUndo.add(temp[1]);
                }

                if (temp[0].equals("finaliza") && !finalizadasRedo.contains(temp[1])) {
                    finalizadas.add(temp[1]);
                    if (!checkpoint) {
                        listRedo.add(temp[1]);
                        finalizadasRedo.add(temp[1]);
                        LogsService.persistLogDatabase("finaliza-redo," + temp[1] + ",,,,," + Utils.formatDateTime(Instant.now().getEpochSecond()));
                        LogsService.saveLogsOnDatabase();
                    }
                }

                if (temp[0].equals("update") && finalizadasRedo.contains(temp[1]) && !updatedRedo.contains(temp[1])) {
                    updateRedo.add(transactions.get(i - 1));
                }

                if (!finalizadas.contains(temp[1]) && (temp[0].equals("inicia") || temp[0].equals("update")) && !updatedUndo.contains(temp[1])) {
                    ativas.add(transactions.get(i - 1));
                    if (!listUndo.contains(temp[1])) addToListUndo(temp[1]);
                }

                if(temp[0].equals("update") && listUndo.contains(temp[1]) && !updatedUndo.contains(temp[1])){
                    updateUndo.add(transactions.get(i - 1));
                }
            }

            System.out.println("Update redo");
            if (!updateRedo.isEmpty()) {
                List<String> updateRedoInvertida = new ArrayList<>(updateRedo);
                Collections.reverse(updateRedoInvertida);

                for (String st : updateRedoInvertida) {
                    String[] temp = st.split(",");
                    System.out.println(st);

                    int index = Integer.parseInt(temp[2]);
                    Product product = productsBuffer.get(index);

                    switch (temp[3]) {
                        case "quantidade":
                            product.setQuantity(Integer.parseInt(temp[5]));
                            break;
                        case "preço":
                            product.setPrice(new BigDecimal(temp[5]));
                            break;
                        case "nome":
                            product.setName(temp[5]);
                            break;
                    }

                    productsBuffer.replace(index, product);
                    Database.getInstance().saveOnFile();
                    LogsService.persistLogDatabase("updated-redo," + temp[1] + ",,,,," + Utils.formatDateTime(Instant.now().getEpochSecond()));
                }
            }


            System.out.println("Update undo");
            if (!updateUndo.isEmpty()) {

                for (String st : updateUndo) {
                    String[] temp = st.split(",");
                    System.out.println(st);

                    int index = Integer.parseInt(temp[2]);
                    Product product = productsBuffer.get(index);

                    switch (temp[3]) {
                        case "quantidade":
                            product.setQuantity(Integer.parseInt(temp[4]));
                            break;
                        case "preço":
                            product.setPrice(new BigDecimal(temp[4]));
                            break;
                        case "nome":
                            product.setName(temp[4]);
                            break;
                    }

                    productsBuffer.replace(index, product);
                    Database.getInstance().saveOnFile();
                    LogsService.persistLogDatabase("updated-undo," + temp[1] + ",,,,," + Utils.formatDateTime(Instant.now().getEpochSecond()));
                }
            }


            System.out.println("\nReports do recovery");

            System.out.println("\nLista undo");
            if (!listUndo.isEmpty()) {
                for (String st : listUndo) {
                    System.out.println(st);
                }
            } else {
                System.out.println(" - Lista vazia");
            }

            System.out.println("\nLista redo");
            if (!listRedo.isEmpty()) {
                for (String st : listRedo) {
                    System.out.println(st);
                }
            } else {
                System.out.println(" - Lista vazia");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
