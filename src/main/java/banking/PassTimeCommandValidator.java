package banking;

class PassTimeCommandValidator {
    private Bank bank;
    PassTimeCommandValidator(Bank bank) {
        this.bank = bank;
    }

    boolean validate(String[] commandArguments) {
        return false;
    }
}
