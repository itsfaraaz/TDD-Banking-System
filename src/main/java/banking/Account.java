package banking;

public class Account {

    double balance;

    public double getBalance() {
        return balance;
    }

    void deposit(double balance) {
        this.balance += balance;
    }

    public void withdraw(double balance) {
        this.balance -= balance;
    }

    public void setBalance(double i) {
        this.balance = i;
    }
}
