import java.util.Random;

public class BankAccount {
    public static void main(String[] args) {
        Account person = new Account("Олег", "Щегольков", 0);
        person.personOleg();

        Thread replenishmentOfBalance = new Thread(() -> {
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                try {
                    double amount = random.nextDouble(50, 100);
                    person.replenishmentOfBalance(amount);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        replenishmentOfBalance.start();

        person.withdrawalOfMoney(500);

        System.out.println("\u001B[34m" + "Конечный баланс: " + person.endBalance() + " руб." + "\u001B[0m");

        try {
            replenishmentOfBalance.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}