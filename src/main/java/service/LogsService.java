package service;

import entities.Logs;

import java.io.*;
import java.util.List;

import static utils.Constants.CABECALHO;
import static utils.Constants.LOGS_FILE_PATH;

public class LogsService {

    public static void showLogsBuffer() {
        System.out.println("\n Lista de logs no buffer:");
        List<String> logsBuffer = getLogsBuffer();
        if (logsBuffer.isEmpty()) {
            System.out.println(" Buffer vazio");
        } else {
            System.out.println(CABECALHO);
            for (String log : logsBuffer) {
                System.out.println(log);
            }
        }
    }

    public static void showLogsOnDatabase() {
        getLogsFromLogsFile();
        List<String> logsDatabase = getLogsDatabase();
        if (logsDatabase.isEmpty()) {
            System.out.println("Não há logs salvos em disco");
        } else {
            System.out.println("\n Lista de logs em memória");
            System.out.println(CABECALHO);
            for (String log : logsDatabase) {
                System.out.println(log);
            }
        }
    }

    public static void getLogsFromLogsFile() {
        List<String> logsDatabase = getLogsDatabase();
        try (BufferedReader br = new BufferedReader(new FileReader(LOGS_FILE_PATH))) {
            String line = br.readLine(); //atribuido valor para ler a primeira linha com os nomes dos parâmentros

            while ((line = br.readLine()) != null) {
                if (!logsDatabase.contains(line)) {
                    addToLogDatabase(line);
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

    public static void saveLogsOnLogsFile() {
        List<String> logsDatabase = getLogsDatabase();
        List<String> logsBuffer = getLogsBuffer();

        if (!logsBuffer.isEmpty()) {
            for (String log : logsBuffer) {
                if (!logsDatabase.contains(log)) {
                    logsDatabase.add(log);
                }
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOGS_FILE_PATH))) {
            bw.write(CABECALHO);
            bw.newLine();
            for (String log : logsDatabase) {
                bw.write(log);
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        getLogsFromLogsFile();
    }

    public static void addToLogBuffer(String log) {
        Logs.addLogBuffer(log);
    }

    public static void addToLogDatabase(String log) {
        Logs.addLogDatabase(log);
    }

    public static void clearLogsBuffer() {
        Logs.clearLogsBuffer();
    }

    public static void clearLogsDatabase() {
        Logs.clearLogsDatabase();
    }

    public static List<String> getLogsBuffer() {
        return Logs.getLogsBuffer();
    }

    public static List<String> getLogsDatabase() {
        return Logs.getLogsDatabase();
    }

    public static void reloadLogsDatabase() {
        getLogsFromLogsFile();
    }

}
