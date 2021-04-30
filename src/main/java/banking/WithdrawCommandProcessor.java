package banking;

class WithdrawCommandProcessor {
    private Bank bank;

    WithdrawCommandProcessor(Bank bank) {
        this.bank = bank;
    }

    void execute(String[] commandArguments) {

        String accountID = commandArguments[1];
        double amount = Double.parseDouble(commandArguments[2]);
        bank.withdrawFrom(accountID, amount);

    }
}
