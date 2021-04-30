package banking;

class CommandProcessor {

    private final Bank bank;
    CommandProcessor(Bank bank) {
        this.bank = bank;
    }

    public void execute(String[] commandArguments) {
        if (commandArguments[0].equalsIgnoreCase("create")) {
            CreateCommandProcessor processor = new CreateCommandProcessor(bank);
            processor.execute(commandArguments);
        } else if (commandArguments[0].equalsIgnoreCase("withdraw")) {
            WithdrawCommandProcessor processor = new WithdrawCommandProcessor(bank);
            processor.execute(commandArguments);
        } else if (commandArguments[0].equalsIgnoreCase("deposit")) {
            DepositCommandProcessor processor = new DepositCommandProcessor(bank);
            processor.execute(commandArguments);
        } else if (commandArguments[0].equalsIgnoreCase("pass")) {
            PassTimeCommandProcessor processor = new PassTimeCommandProcessor(bank);
            processor.execute(commandArguments);
        } else if (commandArguments[0].equalsIgnoreCase("transfer")) {
            TransferCommandProcessor processor = new TransferCommandProcessor(bank);
            processor.execute(commandArguments);
        }
    }
}
