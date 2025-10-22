import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private double balance;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    String name;
    String surName;

    Account(String name, String surName, double balance) {
        this.name = name;
        this.surName = surName;
        this.balance = balance;
    }

    void personOleg() {
        System.out.println( "\u001B[36m" + this.name + " " + this.surName + ": " + this.balance + " руб." + "\u001B[0m");
    }

    public void replenishmentOfBalance(double amount) {
        lock.lock();
        try {
            balance += amount;
            balance = Math.round(balance * 100.0) / 100.0;
            amount = Math.round(amount * 100.0) / 100.0;
            System.out.println("\u001B[32m" + "Пополнение: " + amount + " руб., баланс: " + balance + " руб." + "\u001B[0m");
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void withdrawalOfMoney(double targetAmount) {
        lock.lock();
        try {
            while (balance < targetAmount) {
                balance = Math.round(balance * 100.0) / 100.0;
                targetAmount = Math.round(targetAmount * 100.0) / 100.0;
                System.out.println("\u001B[35m" + "Текущий баланс: " + balance + " руб., требуется: " + targetAmount + " руб." + "\u001B[0m");
                condition.await();
            }

            balance -= targetAmount;
            balance = Math.round(balance * 100.0) / 100.0;
            System.out.println("\u001B[33m" + "Снятие: " + targetAmount + " руб., остаток: " + balance + " руб." + "\u001B[0m");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public double endBalance() {
        lock.lock();
        balance = Math.round(balance * 100.0) / 100.0;
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }
}
