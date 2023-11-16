package service;

import entities.Database;
import entities.Product;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;

import static utils.Constants.STOCK_FILE_PATH;

public class DatabaseService {
    public static void showProductsBuffer() {
        System.out.println("\n Lista de produtos no buffer");
        for (Product product : Database.getProductsBuffer().values()) {
            System.out.println(product.toString());
        }
    }

    public static void showProductsDatabase(){
        System.out.println("\n Lista de produtos no disco");
        for (Product product : getProductsFromFile().values()) {
            System.out.println(product.toString());
        }
    }

    public static HashMap<Integer, Product> getProductsFromFile() {
        HashMap<Integer, Product> products = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(STOCK_FILE_PATH))) {
            String line = reader.readLine();
            while ((line = reader.readLine())!= null) {
                String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                Product product = new Product(
                        Integer.parseInt(fields[0]),
                        fields[1],
                        new BigDecimal(fields[2]),
                        Integer.parseInt(fields[3])
                );
                products.put(product.getId(), product);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return products;
    }

    public static void bufferingProductsBuffer() {
        Database.setProductsBuffer(getProductsFromFile());
    }

    public static void bufferingProductsDatabase() {
        Database.setProductsDatabase(getProductsFromFile());
    }

    public static void reloadProductsDatabaseAndBuffer() {
        bufferingProductsDatabase();
        bufferingProductsBuffer();
    }

    public static void saveProductsFromBufferToDatabase() {
        setProductsDatabase(getProductsBuffer());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STOCK_FILE_PATH))) {
            bw.write("id, name, price, quantity");
            bw.newLine();
            for (Product product : getProductsBuffer().values()) {
                bw.write(product.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void databaseClear(){
        Database.productBufferClear();
        Database.productsDatabaseClear();
    }

    public static void replaceProductBuffer(int index, Product product){
        Database.getProductsBuffer().replace(index, product);
    }

    public static void replaceProductDatabase(int index, Product product){
        Database.getProductsDatabase().replace(index, product);
    }

    public static Product getProductBuffer(int index){
        return Database.getProductsBuffer().get(index);
    }

    public static HashMap<Integer, Product> getProductsBuffer(){
        return Database.getProductsBuffer();
    }

    public static HashMap<Integer, Product> getProductsDatabase(){
        return Database.getProductsDatabase();
    }

    public static void setProductsBuffer(HashMap<Integer, Product> hashmap){
        Database.setProductsBuffer(hashmap);
    }

    public static void setProductsDatabase(HashMap<Integer, Product> products) {
        Database.setProductsDatabase(products);
    }
}
