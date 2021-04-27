package banking;

class DepositCommandValidator {
    private Bank bank;
    DepositCommandValidator(Bank bank) {
        this.bank = bank;
    }

    boolean validate(String[] commandArguments) {
        if (validateThatAccountExistsInBank(commandArguments)) {
            if (isDepositingIntoCheckingAccount(commandArguments)) {
                return depositAmountIntoCheckingAccountIsValid(commandArguments);
            } else if (isDepositingIntoSavingsAccount(commandArguments)) {
                return depositAmountIntoSavingsAccountIsValid(commandArguments);
            } else if (isDepositingIntoCDAccount(commandArguments)) {
                return false;
            }
        }
        return false;
    }

    private boolean validateThatAccountExistsInBank(String[] commandArguments) {
        try {
            return bank.getAccounts().containsKey(commandArguments[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private boolean isDepositingIntoCheckingAccount(String[] commandArguments) {
        try {
            Account account  = bank.getAccounts().get(commandArguments[1]);
            return account instanceof CheckingAccount;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private boolean isDepositingIntoSavingsAccount(String[] commandArguments) {
        try {
            Account account  = bank.getAccounts().get(commandArguments[1]);
            return account instanceof SavingsAccount;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private boolean isDepositingIntoCDAccount(String[] commandArguments) {
        try {
            Account account  = bank.getAccounts().get(commandArguments[1]);
            return account instanceof CDAccount;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private boolean depositAmountIntoCheckingAccountIsValid(String[] commandArguments) {
        double depositAmount = isDouble(commandArguments[2]);
        return (depositAmount != -1 && depositAmount >= 0 && depositAmount <= 1000);
    }

    private boolean depositAmountIntoSavingsAccountIsValid(String[] commandArguments) {
        double depositAmount = isDouble(commandArguments[2]);
        return (depositAmount != -1 && depositAmount >= 0 && depositAmount <= 2500);
    }

    private double isDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
