package banking;

public class PassTimeCommandProcessor {

    private Bank bank;

    PassTimeCommandProcessor(Bank bank) {
        this.bank = bank;
    }

    void execute(String[] commandArguments) {
        int months = Integer.parseInt(commandArguments[1]);
        bank.passTime(months);
    }
}
