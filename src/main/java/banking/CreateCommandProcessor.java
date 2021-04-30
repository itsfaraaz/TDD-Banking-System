package banking;

class CreateCommandProcessor {

    private final Bank bank;

    CreateCommandProcessor(Bank bank) {
        this.bank = bank;
    }

    void execute(String[] commandArguments) {
        if (commandArguments[1].equalsIgnoreCase("checking")) {
            double apr = Double.parseDouble(commandArguments[3]);
            bank.openCheckingAccount(commandArguments[2], apr);
        } else if (commandArguments[1].equalsIgnoreCase("cd")) {
            double apr = Double.parseDouble(commandArguments[3]);
            double startingBalance = Double.parseDouble(commandArguments[4]);
            bank.openCDAccount(commandArguments[2], apr, startingBalance);
        } else if (commandArguments[1].equalsIgnoreCase("savings")) {
            double apr = Double.parseDouble(commandArguments[3]);
            bank.openSavingsAccount(commandArguments[2], apr);
        }
    }
}
