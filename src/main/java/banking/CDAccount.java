package banking;

public class CDAccount extends Account {

    private String id;
    private double apr;

    CDAccount(String id, double apr, double balance) {
        this.id = id;
        this.apr = apr;
        super.balance = balance;
    }

    String getID() {
        return id;
    }

    double getAPR() {
        return apr;
    }

    public Boolean canTransfer() {
        return false;
    }
}
