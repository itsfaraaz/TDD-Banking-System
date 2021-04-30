package banking;

public class CDAccount extends Account {

    CDAccount(String id, double apr, double balance) {
        super.id = id;
        super.apr = apr;
        super.balance = balance;
        super.accountType = "Cd";
    }

    public Boolean canTransfer() {
        return false;
    }
}
