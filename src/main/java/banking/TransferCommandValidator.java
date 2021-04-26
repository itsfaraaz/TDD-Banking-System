package banking;

class TransferCommandValidator {

    private Bank bank;

    TransferCommandValidator(Bank bank) {
        this.bank = bank;
    }

    boolean validate(String[] commandArguments) {
            return false;
        }

}
