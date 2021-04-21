package banking;

public class CheckingAccount extends Account {

    private String id;
    private double apr;

    CheckingAccount(String id, double apr) {
        this.id = id;
        this.apr = apr;
        super.balance = 0;
    }

    String getID() {
        return id;
    }

    double getAPR() {
        return apr;
    }
}
