package banking;

import java.util.HashMap;

public class Bank {

    private HashMap<String, Account> accounts = new HashMap<>();

    public HashMap<String, Account> getAccounts() {
        return accounts;
    }

    public void openCheckingAccount(String id, double apr) {
        CheckingAccount account = new CheckingAccount(id, apr);
        accounts.put(id, account);
    }

    public void openSavingsAccount(String id, double apr) {
        SavingsAccount account = new SavingsAccount(id, apr);
        accounts.put(id, account);
    }

    public void openCDAccount(String id, double apr, int balance) {
        CDAccount account = new CDAccount(id, apr, balance);
        accounts.put(id, account);
    }
}
