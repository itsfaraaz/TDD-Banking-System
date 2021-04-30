package banking;

class PassTimeCommandValidator {
    private Bank bank;
    PassTimeCommandValidator(Bank bank) {
        this.bank = bank;
    }

    boolean validate(String[] commandArguments) {
        return validateTheNumberOfGivenArguments(commandArguments)
                && validateThatGivenIntegerIsWithinBounds(commandArguments);
    }

    private boolean validateTheNumberOfGivenArguments(String[] commandArguments) {
        return commandArguments.length == 2;
    }

    private int validateThatSecondArgumentIsInteger(String[] commandArguments) {
        try {
            return Integer.parseInt(commandArguments[1]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean validateThatGivenIntegerIsWithinBounds(String[] commandArguments) {
        int months = validateThatSecondArgumentIsInteger(commandArguments);
        return (months >= 1 && months <= 60);
    }
}
