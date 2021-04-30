package banking;

class Account {

    double balance;
    int age = 0;
    String id;
    double apr;
    String accountType;

    double getBalance() {
        return balance;
    }

    void deposit(double balance) {
        this.balance += balance;
    }

    void withdraw(double balance) {
        this.balance -= balance;
    }

    void setBalance(double i) {
        this.balance = i;
    }

    void setMonths(int months) {
        this.age = months;
    }

    String getID() {
        return id;
    }

    double getAPR() {
        return apr;
    }

    int getMonths() {
        return age;
    }

    void passTimeAndCalculateAPR(int months) {
        age += months;

        if (accountType.equalsIgnoreCase("cd")) {
            for (int monthLoopIndex = 0; monthLoopIndex < months; monthLoopIndex++) {
                for (int multipleCalculationCounter = 0; multipleCalculationCounter < 4; multipleCalculationCounter++) {
                    balance += ((apr / 100) / 12) * balance;
                }
            }
        } else {
            for (int monthLoopIndex = 0; monthLoopIndex < months; monthLoopIndex++) {
                balance += ((apr / 100) / 12) * balance;
            }
        }
    }

    public String getAccountType() {
        return accountType;
    }
}
