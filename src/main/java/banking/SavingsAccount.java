package banking;

class SavingsAccount extends Account {

    private boolean alreadyMadeWithdrawalThisMonth = false;

    SavingsAccount(String id, double apr) {
        super.id = id;
        super.apr = apr;
        super.balance = 0;
        super.accountType = "Savings";
    }

    void setMonths(int months) {
        if (months > super.age) {
            alreadyMadeWithdrawalThisMonth = false;
        }
        super.age = months;
    }

    void changeWithdrawalStatus() {
        alreadyMadeWithdrawalThisMonth = !alreadyMadeWithdrawalThisMonth;
    }

    boolean getAlreadyMadeWithdrawalThisMonth() {
        return alreadyMadeWithdrawalThisMonth;
    }

}
