package banking;

class DepositCommandValidator {
    private Bank bank;
    DepositCommandValidator(Bank bank) {
        this.bank = bank;
    }

    boolean validate(String[] commandArguments) {
        return false;
    }
}
