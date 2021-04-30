package banking;

import static banking.DepositCommandValidator.*;

class TransferCommandValidator {

    private Bank bank;
    private CommandValidator validator;

    TransferCommandValidator(Bank bank) {
        this.bank = bank;
        validator = new CommandValidator(bank);
    }

    boolean validate(String[] commandArguments) {
        if (validateThatAccountExistsInBank(commandArguments, bank, 1) && validateThatAccountExistsInBank(commandArguments, bank, 2)) {
            if (commandArguments[1].equals(commandArguments[2])) {
                return false;
            }

            if (isReferencingCDAccount(commandArguments, bank, 1) || isReferencingCDAccount(commandArguments, bank, 2)) {
                return false;
            }

            return validateTransferConformsToDepositRules(commandArguments) && validateTransferConformsToWithdrawalRules(commandArguments);
        }
        return false;
    }

    private boolean validateTransferConformsToDepositRules(String[] commandArguments) {
        String accountIDToDepositTo = commandArguments[2];
        String amountToTransfer = commandArguments[3];
        return validator.validate(new String[]{"deposit", accountIDToDepositTo, amountToTransfer});
    }

    private boolean validateTransferConformsToWithdrawalRules(String[] commandArguments) {
        String accountIDToWithdrawFrom = commandArguments[1];
        String amountToTransfer = commandArguments[3];
        return validator.validate(new String[]{"withdraw", accountIDToWithdrawFrom, amountToTransfer});
    }

}
