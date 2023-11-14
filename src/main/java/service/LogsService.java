package service;

import entities.Logs;

import java.io.*;
import java.util.List;

import static utils.Constants.CABECALHO;

public class LogsService {

    private static final Logs logs = Logs.getInstance();
    private static final List<String> logsBuffer = logs.getLogsBuffer();
    private static final List<String> logsDatabase = logs.getLogsDatabase();

    public static void showLogsBuffer() {
        System.out.println("\n Lista de logs no buffer:");
        if (logsBuffer.isEmpty()) {
            System.out.println(" Buffer vazio");
        }else{
            System.out.println(CABECALHO);
            for (String log : logsBuffer) {
                System.out.println(log);
            }
        }
    }

    public static void showLogsOnDatabase() {
        logsFromDatabase();
        if(logsDatabase.isEmpty()){
            System.out.println("Não há logs salvos em disco");
        }else{
            System.out.println("\n Lista de logs em memória");
            System.out.println(CABECALHO);
            for (String log : logsDatabase) {
                System.out.println(log);
            }
        }
    }


    public static void logsFromDatabase() {
        String path = "src/resources/files/logs.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine(); //atribuido valor para ler a primeira linha com os nomes dos parâmentros

            while ((line = br.readLine()) != null) {
                if(!logsDatabase.contains(line)){
                    logsDatabase.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("arquivo não encontrado");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("problema na leitura do arquivo");
            e.printStackTrace();
        }
    }

    public static void saveLogsOnDatabase() {
        String path = "src/resources/files/logs.csv";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(CABECALHO);
            bw.newLine();
            for (String log : logsDatabase) {
                bw.write(log);
                bw.newLine();
            }
            for (String log : logsBuffer) {
                if(!logsDatabase.contains(log)){
                    bw.write(log);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void persistLogBuffer(String log) {
        Logs.getInstance().persistLogBuffer(log);
    }

    public static void persistLogDatabase(String log) {
        Logs.getInstance().persistLogDatabase(log);
    }

    public static void clearLogsBuffer(){
        logsBuffer.clear();
    }
}
