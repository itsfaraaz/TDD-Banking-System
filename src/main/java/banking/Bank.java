package banking;

import java.util.HashMap;

public class Bank {

    HashMap<String, Account> accounts = new HashMap<>();

    HashMap<String, Account> getAccounts() {
        return accounts;
    }

    void openCheckingAccount(String id, double apr) {
        CheckingAccount account = new CheckingAccount(id, apr);
        accounts.put(id, account);
    }

    void openSavingsAccount(String id, double apr) {
        SavingsAccount account = new SavingsAccount(id, apr);
        accounts.put(id, account);
    }

    void openCDAccount(String id, double apr, double balance) {
        CDAccount account = new CDAccount(id, apr, balance);
        accounts.put(id, account);
    }

    void depositInto(String id, double balance) {
        accounts.get(id).deposit(balance);
    }

    double withdrawFrom(String id, double balance) {
        Account account = accounts.get(id);
        if (account.getBalance() < balance) {
            double previousBalance =  account.getBalance();
            account.setBalance(0);
            return previousBalance;
        } else {
            account.withdraw(balance);
            return balance;
        }
    }

    public void transferBetweenAccounts(String fromAccount, String toAccount, double amount) {
        accounts.get(toAccount).deposit(withdrawFrom(fromAccount, amount));
    }
}
