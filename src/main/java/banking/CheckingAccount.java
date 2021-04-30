package banking;

public class CheckingAccount extends Account {

    CheckingAccount(String id, double apr) {
        super.id = id;
        super.apr = apr;
        super.balance = 0;
        super.accountType = "Checking";
    }
}
