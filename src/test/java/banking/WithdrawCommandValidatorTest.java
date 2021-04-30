package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WithdrawCommandValidatorTest {

    private CommandValidator commandValidator;
    private Bank bank;
    private CommandInput commandInput;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);
        commandInput = new CommandInput();
    }

    @Test
    void invalid_empty_command() {
        String[] commandArgs = commandInput.commandToArray("");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void missing_account_id_and_deposit_amount() {
        String[] commandArgs = commandInput.commandToArray("withdraw");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void missing_withdraw_amount() {
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void given_account_id_does_not_exist_in_bank() {
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 400");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void account_id_less_than_eight_digits_is_invalid() {
        bank.openCheckingAccount("12345678", 3.12);
        String[] commandArgs = commandInput.commandToArray("withdraw 1234567 400");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void account_id_greater_than_eight_digits_is_invalid() {
        bank.openCheckingAccount("12345678", 0.51);
        String[] commandArgs = commandInput.commandToArray("withdraw 123456789 400");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void account_id_is_not_a_number_is_invalid() {
        bank.openCheckingAccount("12345678", 0.12);
        String[] commandArgs = commandInput.commandToArray("withdraw 1abc5678 400");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void able_to_withdraw_zero_dollars_from_savings_account() {
        bank.openSavingsAccount("12345678", 0.41);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 0");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertTrue(isValidCommand);
    }

    @Test
    void can_withdraw_one_dollar_from_savings_account() {
        bank.openSavingsAccount("12345678", 0.14);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 1");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertTrue(isValidCommand);
    }

    @Test
    void cannot_withdraw_negative_amount_from_savings_account() {
        bank.openSavingsAccount("12345678", 0.21);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 -1");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void can_withdraw_max_one_thousand_dollars_from_savings_account() {
        bank.openSavingsAccount("12345678", 0.34);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 1000");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertTrue(isValidCommand);
    }

    @Test
    void cannot_withdraw_more_than_one_thousand_dollars_from_savings_account() {
        bank.openSavingsAccount("12345678", 0.19);
        bank.depositInto("12345678", 500);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 1001");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void cannot_withdraw_from_savings_account_more_than_once_a_month() {
        bank.openSavingsAccount("12345678", 0.91);
        bank.depositInto("12345678", 500);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 200");
        assertTrue(commandValidator.validate(commandArgs));
        commandArgs = commandInput.commandToArray("withdraw 8765321 300");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void can_withdraw_from_account_when_at_least_one_month_passes() {
        bank.openSavingsAccount("12345678", 1.13);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 200");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertTrue(isValidCommand);
        bank.getAccounts().get("12345678").setMonths(3);
        commandArgs = commandInput.commandToArray("withdraw 12345678 300");
        isValidCommand = commandValidator.validate(commandArgs);
        assertTrue(isValidCommand);
    }

    @Test
    void can_withdraw_zero_dollars_from_checking_account() {
        bank.openCheckingAccount("12345678", 1.1);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 0");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertTrue(isValidCommand);
    }

    @Test
    void can_withdraw_one_dollar_from_checking_account() {
        bank.openCheckingAccount("12345678", 0.91);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 1");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertTrue(isValidCommand);
    }

    @Test
    void cannot_withdraw_negative_amount_from_checking_account() {
        bank.openCheckingAccount("12345678", 0.12);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 -1");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void can_withdraw_max_four_hundred_dollars_from_checking_account() {
        bank.openCheckingAccount("12345678", 0.31);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 400");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertTrue(isValidCommand);
    }

    @Test
    void cannot_withdraw_more_then_four_hundred_dollars_from_checking_account() {
        bank.openCheckingAccount("12345678", 1.01);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 401");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void can_withdraw_twice_from_checking_account_twice_a_month() {
        bank.openCheckingAccount("12345678", 1.11);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 100");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertTrue(isValidCommand);
        commandArgs = commandInput.commandToArray("withdraw 12345678 100");
        isValidCommand = commandValidator.validate(commandArgs);
        assertTrue(isValidCommand);
    }

    @Test
    void cannot_withdraw_from_CD_account_that_is_younger_than_twelve_months() {
        bank.openCDAccount("12345678", 0.01, 1250);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 1250");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void can_withdraw_from_CD_account_at_least_twelve_months_old_and_amount_is_equal_to_balance() {
        bank.openCDAccount("12345678", 0.01, 1250);
        bank.getAccounts().get("12345678").setMonths(12);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 1250");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertTrue(isValidCommand);
    }

    @Test
    void cannot_withdraw_zero_dollars_which_is_less_than_balance_from_CD_account_at_least_twelve_months_old() {
        bank.openCDAccount("12345678", 0.1, 1250);
        bank.getAccounts().get("12345678").setMonths(12);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 0");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void cannot_withdraw_one_dollar_which_is_less_than_balance_from_CD_account_at_least_twelve_months_old() {
        bank.openCDAccount("12345678", 0.1, 1250);
        bank.getAccounts().get("12345678").setMonths(12);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 1");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void cannot_withdraw_negative_amount_from_CD_account_that_is_at_least_twelve_months_old() {
        bank.openCDAccount("12345678", 0.1, 1250);
        bank.getAccounts().get("12345678").setMonths(12);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 -1");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void can_withdraw_amount_greater_than_balance_from_CD_account_that_is_at_least_twelve_months_old() {
        bank.openCDAccount("12345678", 0.1, 1250);
        bank.getAccounts().get("12345678").setMonths(12);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 1250");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertTrue(isValidCommand);
    }

    @Test
    void withdraw_amount_has_unexpected_symbol() {
        bank.openSavingsAccount("12345678", 0.10);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 1,200");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void withdraw_amount_is_not_a_double() {
        bank.openCheckingAccount("12345678", 0.50);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 abc");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

    @Test
    void withdraw_amount_has_unexpected_letters() {
        bank.openCheckingAccount("12345678", 0.1);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 28ab");
        boolean isValidCommand = commandValidator.validate(commandArgs);
        assertFalse(isValidCommand);
    }

}
