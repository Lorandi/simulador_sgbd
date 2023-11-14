package entities;

import enums.TransactionStatus;

import java.time.Instant;
import java.util.Objects;

public class Transaction {
    private static Integer counter = 0;

    private final Integer id;
    private final String name;
    private TransactionStatus status;

    public Transaction() {

        counter++;
        this.id = counter;
        this.name = "T"+ Instant.now().getEpochSecond();
        this.status = TransactionStatus.STARTED;
        System.out.println("\nTransação " + this.name + " sendo criada...");
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TransactionStatus getStatus() {
        return status;
    }
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return  name ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction product = (Transaction) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}