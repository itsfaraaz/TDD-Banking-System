package banking;

class PassTimeCommandProcessor {

    private final Bank bank;

    PassTimeCommandProcessor(Bank bank) {
        this.bank = bank;
    }

    void execute(String[] commandArguments) {
        int months = Integer.parseInt(commandArguments[1]);
        bank.passTime(months);
    }
}
