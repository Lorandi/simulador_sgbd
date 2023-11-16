package entities;


import java.util.HashMap;

public abstract class Database {
    private static HashMap<Integer, Product> productsBuffer = new HashMap<>();
    private static HashMap<Integer, Product> productsDatabase = new HashMap<>();

    public static HashMap<Integer, Product> getProductsBuffer() {
        return productsBuffer;
    }

    public static HashMap<Integer, Product> getProductsDatabase() {
        return productsDatabase;
    }

    public static void setProductsBuffer(HashMap<Integer, Product> hashmap) {
        productsBuffer = hashmap;
    }

    public static void setProductsDatabase(HashMap<Integer, Product> hashMap) {
        productsDatabase = hashMap;
    }

    public static void productBufferClear(){
        productsBuffer.clear();
    }

    public static void productsDatabaseClear(){
        productsDatabase.clear();
    }

}