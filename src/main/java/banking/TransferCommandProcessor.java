package banking;

class TransferCommandProcessor {
    private Bank bank;

    TransferCommandProcessor(Bank bank) {
        this.bank = bank;
    }

    void execute(String[] commandArguments) {

        String transferFrom = commandArguments[1];
        String transferTo = commandArguments[2];
        double amount = Double.parseDouble(commandArguments[3]);

        bank.transferBetweenAccounts(transferFrom, transferTo, amount);

    }
}
