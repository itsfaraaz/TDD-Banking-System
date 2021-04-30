package banking;

class DepositCommandValidator {
    private Bank bank;
    DepositCommandValidator(Bank bank) {
        this.bank = bank;
    }

    boolean validate(String[] commandArguments) {
        if (validateThatAccountExistsInBank(commandArguments, bank, 1)) {
            if (isReferencingCheckingAccount(commandArguments, bank, 1)) {
                return depositAmountIntoCheckingAccountIsValid(commandArguments);
            } else if (isReferencingSavingsAccount(commandArguments, bank, 1)) {
                return depositAmountIntoSavingsAccountIsValid(commandArguments);
            } else if (isReferencingCDAccount(commandArguments, bank, 1)) {
                return false;
            }
        }
        return false;
    }

    static boolean validateThatAccountExistsInBank(String[] commandArguments, Bank bank, int accountIDIndex) {
        try {
            return bank.getAccounts().containsKey(commandArguments[accountIDIndex]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    static boolean isReferencingCheckingAccount(String[] commandArguments, Bank bank, int accountIDIndex) {
        try {
            Account account  = bank.getAccounts().get(commandArguments[accountIDIndex]);
            return account instanceof CheckingAccount;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    static boolean isReferencingSavingsAccount(String[] commandArguments, Bank bank, int accountIDIndex) {
        try {
            Account account  = bank.getAccounts().get(commandArguments[accountIDIndex]);
            return account instanceof SavingsAccount;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    static boolean isReferencingCDAccount(String[] commandArguments, Bank bank, int accountIDIndex) {
        try {
            Account account  = bank.getAccounts().get(commandArguments[accountIDIndex]);
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

    static double isDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
