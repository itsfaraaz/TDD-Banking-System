package banking;

class CreateCommandValidator {
    private final Bank bank;
    CreateCommandValidator(Bank bank) {
        this.bank = bank;
    }

    boolean validate(String[] commandArguments) {
        if (isCreatingCheckingAccount(commandArguments) || isCreatingSavingsAccount(commandArguments)) {
            return validateCheckingOrSavingsAccountArguments(commandArguments);
        } else if (isCreatingCDAccount(commandArguments)) {
            return validateCDAccountArguments(commandArguments);
        }
        return false;
    }

    private boolean validateCDAccountArguments(String[] commandArguments) {
        try {
            return creatingCDAccountHasProperNumberOfArguments(commandArguments) &&
                    isAValidAccountID(commandArguments[2]) &&
                    validateThatAccountIDDoesNotExist(commandArguments[2]) &&
                    isValidAPR(commandArguments[3]) &&
                    isWithinCDStartingBalanceLimits(commandArguments[4]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private boolean validateThatAccountIDDoesNotExist(String accountID) {
        return !bank.getAccounts().containsKey(accountID);
    }

    private boolean validateCheckingOrSavingsAccountArguments(String[] commandArguments) {
        try {
            return creatingCheckingOrSavingsHasProperNumberOfArguments(commandArguments) &&
                    validateThatAccountIDDoesNotExist(commandArguments[2]) &&
                    startsWithBalanceOfZero(commandArguments) &&
                    isAValidAccountID(commandArguments[2]) &&
                    isValidAPR(commandArguments[3]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private boolean creatingCDAccountHasProperNumberOfArguments(String[] commandArguments) {
        return commandArguments.length == 5;
    }

    private boolean creatingCheckingOrSavingsHasProperNumberOfArguments(String[] commandArguments) {
        return commandArguments.length == 4;
    }

    private boolean isAValidAccountID(String accountID) {
        if (isInteger(accountID)) {
            return accountID.length() == 8;
        }
        return false;
    }

    private boolean isValidAPR(String APR) {
        double parsedAPR;
        try {
            parsedAPR = Double.parseDouble(APR);
        } catch(NumberFormatException e) {
            return false;
        }
        return(parsedAPR >= 0 && parsedAPR <= 10);
    }

    private boolean isCreatingCheckingAccount(String[] commandArguments) {
        try {
            return commandArguments[1].equalsIgnoreCase("checking");
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private boolean isCreatingSavingsAccount(String[] commandArguments) {
        try {
            return commandArguments[1].equalsIgnoreCase("savings");
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private boolean isCreatingCDAccount(String[] commandArguments) {
        try {
            return commandArguments[1].equalsIgnoreCase("cd");
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private boolean startsWithBalanceOfZero(String[] commandArguments) {
        return commandArguments.length != 5;
    }

    private boolean isWithinCDStartingBalanceLimits(String startingBalacnceAsString) {
        double startingBalance;
        try {
            startingBalance = Double.parseDouble(startingBalacnceAsString);
        } catch(NumberFormatException e) {
            return false;
        }
        return (startingBalance >= 1000 && startingBalance <= 10000);
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
