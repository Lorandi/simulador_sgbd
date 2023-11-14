package service;

import entities.Database;
import entities.Logs;
import entities.Recovery;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
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
            for(int i = 0; i < 3; i++){
                Thread.sleep(500);
                System.out.println("\n     *****  FALHA  *****    \n");
            }
            Recovery.getInstance().removeAllListUndo();
            Recovery.getInstance().removeAllListRedo();
            LogsService.clearLogsBuffer();
            var fail = "fail,,,,,," + Utils.formatDateTime(Instant.now().getEpochSecond());
            LogsService.persistLogDatabase(fail);
            LogsService.saveLogsOnDatabase();
            LogsService.logsFromDatabase();
            Database.productBufferClear();

            Thread.sleep(500);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void recoveryFromDatabaseLogs() {
        try {

            System.out.print("\nRecovery em andamento");
            for(int i = 0; i < 10; i++){
                System.out.print(".");
                Thread.sleep(200);
            }
            System.out.println();

            Database.getInstance().productsBuffer(productsBuffer);
            System.out.print("\nConexão com banco sendo reestabelecida");
            for(int i = 0; i < 10; i++){
                System.out.print(".");
                Thread.sleep(200);
            }

            System.out.println();


            List<String> transactions = Logs.getInstance().getLogsDatabase();

            List<String> ativas = new ArrayList<>();
            List<String> finalizadas = new ArrayList<>();


            if (transactions.isEmpty()) {
                logsFromDatabase();
            }

            for (int i = transactions.size(); i > 0; i--) {
                String[] temp = transactions.get(i - 1).split(",");

                if (temp[0].equals("finaliza")) {
                    finalizadas.add(temp[1]);
                }

                if (!finalizadas.contains(temp[1]) &&
                        (temp[0].equals("inicia") || temp[0].equals("update"))) {
                    ativas.add(transactions.get(i - 1));

                    if (!listUndo.contains(temp[1])) addToListUndo(temp[1]);
                }
            }
            System.out.println("\nReports do recovery");

            System.out.println("\nLista ativas na falha");
            if(!ativas.isEmpty()){
                for (String st : ativas) {
                    System.out.println(st);
                }
            }else{
                System.out.println(" - Lista vazia");
            }


            System.out.println("\nFinalizadas na falha:");
            if(!finalizadas.isEmpty()){
                for (String st : finalizadas) {
                    System.out.println(st);
                }
            }else{
                System.out.println(" - Lista vazia");
            }

            System.out.println("\nLista undo");
            if(!listUndo.isEmpty()){
                for (String st : listUndo) {
                    System.out.println(st);
                }
            }else{
                System.out.println(" - Lista vazia");
            }

            System.out.println("\nLista redo");
            if(!listRedo.isEmpty()){
                for (String st : listRedo) {
                    System.out.println(st);
                }
            }else{
                System.out.println(" - Lista vazia");
            }

            //TODO mudar a transação com prefixo undo
//            System.out.println("Fanzendo undos");{
//                for(String st :  ativas){
//                    String [] ativaSplit =  st.split(",");
//                    for(String transaction : transactions){
//                        if(st.equals(transaction)){
//                            if(ativaSplit[0].equals("update")){
//                                ativaSplit[0]= "undo_update";
//                                transaction = Arrays.toString(ativaSplit);
//                            }
//
//
//                        }
//                    }
//                }
//            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
