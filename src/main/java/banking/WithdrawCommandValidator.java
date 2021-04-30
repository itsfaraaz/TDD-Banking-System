package banking;

import static banking.DepositCommandValidator.*;

class WithdrawCommandValidator {
    private final Bank bank;
    WithdrawCommandValidator(Bank bank) {
        this.bank = bank;
    }

    boolean validate(String[] commandArguments) {
        if (validateThatAccountExistsInBank(commandArguments, bank, 1)) {
            if (isReferencingCheckingAccount(commandArguments, bank, 1)) {
                return withdrawAmountFromCheckingAccountIsValid(commandArguments);
            } else if (isReferencingSavingsAccount(commandArguments, bank, 1)) {
                return withdrawAmountFromSavingsAccountIsValid(commandArguments) && validateMaxOneSavingsAccountWithdrawalPerMonth(commandArguments);
            } else if (isReferencingCDAccount(commandArguments, bank, 1)) {
                return withdrawAmountFromCDAccountIsValid(commandArguments) && validateCDAccountTimeIsAtLeastTwelveMonths(commandArguments);
            }
        }
        return false;
    }

    private boolean withdrawAmountFromSavingsAccountIsValid(String[] commandArguments) {
        double withdrawAmount = isDouble(commandArguments[2]);
        return (withdrawAmount != -1 && withdrawAmount >= 0 && withdrawAmount <= 1000);
    }

    private boolean withdrawAmountFromCheckingAccountIsValid(String[] commandArguments) {
        double withdrawAmount = isDouble(commandArguments[2]);
        return (withdrawAmount != -1 && withdrawAmount >= 0 && withdrawAmount <= 400);
    }

    private boolean withdrawAmountFromCDAccountIsValid(String[] commandArguments) {
        double withdrawAmount = isDouble(commandArguments[2]);
        double currentCDAccountBalance = bank.getAccounts().get(commandArguments[1]).getBalance();
        return withdrawAmount == currentCDAccountBalance || withdrawAmount >= currentCDAccountBalance;
    }

    private boolean validateCDAccountTimeIsAtLeastTwelveMonths(String[] commandArguments) {
        int accountAge = bank.getAccounts().get(commandArguments[1]).getMonths();
        return accountAge >= 12 && accountAge % 12 == 0;
    }

    private boolean validateMaxOneSavingsAccountWithdrawalPerMonth(String[] commandArguments) {
        SavingsAccount account = (SavingsAccount) bank.getAccounts().get(commandArguments[1]);
        return !account.getAlreadyMadeWithdrawalThisMonth();
    }
}
