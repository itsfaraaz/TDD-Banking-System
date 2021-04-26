package banking;

class WithdrawCommandValidator {
    private Bank bank;
    WithdrawCommandValidator(Bank bank) {
        this.bank = bank;
    }

    boolean validate(String[] commandArguments) {
        return false;
    }
}
