package banking;

class CreateCommandValidator {
    private Bank bank;
    CreateCommandValidator(Bank bank) {
        this.bank = bank;
    }

    boolean validate(String[] commandArguments) {
        return false;
    }
}
