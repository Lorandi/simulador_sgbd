package entities;

import enums.OptionsMenu;
import service.*;

import java.util.Scanner;

import static enums.OptionsMenu.*;


public class Menu {

    TransactionService transactionService;


    public Menu(){
        RecoveryService.checkingDatabaseIntegrity();
        this.transactionService = new TransactionService(TransactionService.transactions);
    }

    public void menu(){
        Scanner sc = new Scanner(System.in);

        String menu = "\n     - - - MENU PRINCIPAL- - - \n" +
                "["+ START_TRANSACTION.getValue() +"] " + START_TRANSACTION.getDescription() +"\n" +
                "["+ CHOOSE_TRANSACTION.getValue() +"] " + CHOOSE_TRANSACTION.getDescription() +"\n" +
                "["+ END_TRANSACTION.getValue() +"] " + END_TRANSACTION.getDescription() +"\n" +
                "["+ LOG_BUFFER.getValue() +"] " + LOG_BUFFER.getDescription() +"\n" +
                "["+ LOG_DISC.getValue() +"] " + LOG_DISC.getDescription() +"\n" +
                "["+ DATABASE_BUFFER.getValue() +"] " + DATABASE_BUFFER.getDescription() +"\n" +
                "["+ DATABASE_DISC.getValue() +"] " + DATABASE_DISC.getDescription() +"\n" +
                "["+ CHECK_POINT.getValue() +"] " + CHECK_POINT.getDescription() +"\n" +
                "["+ FAIL.getValue() +"] " + FAIL.getDescription() +"\n" +
                "["+ EXIT.getValue() +"] " + EXIT.getDescription() +"\n"+
                "Digite o número da opção desejada: ";

        System.out.print(menu);
        String option = sc.nextLine();

        switch (OptionsMenu.getByCode(option)) {
            case START_TRANSACTION:
                System.out.println("\n" + transactionService.startTransaction() + " criada com sucesso!");
                break;

            case CHOOSE_TRANSACTION:
                ProductService.editProduct(transactionService.chooseTransaction());
                break;

            case END_TRANSACTION:
                transactionService.finishTransaction(transactionService.chooseTransaction());
                break;

            case LOG_BUFFER:
                LogsService.showLogsBuffer();
                break;

            case LOG_DISC:
                LogsService.showLogsOnDatabase();
                break;

            case DATABASE_BUFFER:
                DatabaseService.showProductsBuffer();
                break;

            case DATABASE_DISC:
                DatabaseService.showProductsDatabase();
                break;

            case CHECK_POINT:
                CheckpointService.checkPoint();
                break;

            case FAIL:
                FailService.fail();
                RecoveryService.recoveryFromDatabaseLogs();
                break;

            case EXIT:
                System.out.println("Até logo!!!");
                break;

            default:
                System.out.println("\nOpção inválida. É necessário escolher uma das opções numéricas do menu.");
        }
        if (!option.equals(EXIT.getValue())) {
            menu();
        }
        sc.close();
    }
}