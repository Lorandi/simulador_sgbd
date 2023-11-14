package service;

import entities.Database;
import entities.Logs;
import entities.Product;
import entities.Transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Scanner;

import static java.util.Objects.isNull;
import static validators.Validators.*;

public class ProductService {

    private static HashMap<Integer, Product> products;

    public ProductService(HashMap<Integer, Product> products) {

        this.products = products;
    }
    public static void editProduct(Transaction transaction) {
        if (isNull(transaction) ) return;

        Database.getInstance().showStockListInMemory();
        StringBuilder log = new StringBuilder();
        log.append("update,").append(transaction.getName()).append(",");

        if (!products.isEmpty()) {
            Scanner sc = new Scanner(System.in);
            Product produtcOnStock = searchProductOnStock();


            // Produto criado caso operação seja cancelada
            Product productTemp = new Product(produtcOnStock.getId(), produtcOnStock.getName(), produtcOnStock.getPrice(), produtcOnStock.getQuantity());
            log.append(produtcOnStock.getId()+",");

            System.out.print("Qual dado quer modificar: [1]Nome [2]Preço [3]Quantidade :");

            String data = sc.next();
            boolean checkData = data.equals("1") || data.equals("2") || data.equals("3") ;

            while (!checkData) {
                System.out.println("Dado inválido");
                System.out.print("Qual dado quer modificar: [1]Nome [2]Preço [3]Quantidade :");

                data = sc.next();
                checkData = data.equals("1") || data.equals("2") || data.equals("3") ;
            }

            switch (data) {
                case "1":
                    sc.nextLine();
                    System.out.print("Digite o novo nome: ");
                    String newNane = sc.nextLine();
                    produtcOnStock.setName(newNane);
                    log.append("nome,").append(productTemp.getName()).append(",").append(produtcOnStock.getName());

                    break;

                case "2":
                    BigDecimal newPrice = validateBigDecimal("");
                    produtcOnStock.setPrice(newPrice);
                    log.append("preço,").append(productTemp.getPrice()).append(",").append(produtcOnStock.getPrice());
                    break;

                case "3":
                    Integer newQuantity = validateIntegers();
                    produtcOnStock.setQuantity(newQuantity);
                    log.append("quantidade,").append(productTemp.getQuantity()).append(",").append(produtcOnStock.getQuantity());
                    break;
            }

            System.out.println("\nProduto modificado \n"
                    + "Id: " + produtcOnStock.getId() + ", "
                    + "Nome: " + produtcOnStock.getName() + ", "
                    + "Preço: " + produtcOnStock.getPrice() + ", "
                    + "Quantidade: " + produtcOnStock.getQuantity() + "\n");

            if (confirmOperation()) {
                log.append(",").append(Utils.formatDateTime(Instant.now().getEpochSecond()));
                Logs.getInstance().persistLogBuffer(log.toString());
                System.out.println("Operação realizada com sucesso");

            } else {
                switch (data) {
                    case "1":
                        produtcOnStock.setName(productTemp.getName());
                        break;

                    case "2":
                        produtcOnStock.setPrice(productTemp.getPrice());
                        break;

                    case "3":
                        produtcOnStock.setQuantity(productTemp.getQuantity());
                        break;
                }
                System.out.println("Operação cancelada");
            }
        } else {
            System.out.println("Lista ainda não tem produtos");
        }
        Database.getInstance().showStockListInMemory();
    }
}