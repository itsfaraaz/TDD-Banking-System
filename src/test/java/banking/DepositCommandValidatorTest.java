package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

 class DepositCommandValidatorTest {

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
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void missing_account_id_and_deposit_amount() {
        String[] commandArgs = commandInput.commandToArray("deposit");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void missing_deposit_amount() {
        String[] commandArgs = commandInput.commandToArray("deposit 12345678");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void given_account_id_does_not_exist_in_bank() {
        String[] commandArgs = commandInput.commandToArray("deposit 98765432 100");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void account_id_less_than_eight_digits_is_invalid() {
        bank.openCheckingAccount("12345678", 0.1);
        String[] commandArgs = commandInput.commandToArray("deposit 1234567 100");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void account_id_greater_than_eight_digits_is_invalid() {
        bank.openCheckingAccount("12345678", 0.1);
        String[] commandArgs = commandInput.commandToArray("deposit 123456789 100");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void account_id_is_not_a_number_is_invalid() {
        bank.openCheckingAccount("12345678", 0.1);
        String[] commandArgs = commandInput.commandToArray("deposit 123abcd8 2000");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_deposit_into_a_cd_account() {
        bank.openCDAccount("12345678", 0.1, 1000);
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 100");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void able_to_deposit_zero_dollars_into_savings() {
        bank.openSavingsAccount("12345678", 500);
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 0");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void able_to_deposit_one_dollar_into_savings() {
        bank.openSavingsAccount("12345678", 0.07);
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 1");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_deposit_negative_dollars_into_savings() {
        bank.openSavingsAccount("12345678", 0.78);
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 -1");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void able_to_deposit_twenty_five_hundred_dollars_into_savings_account() {
        bank.openSavingsAccount("12345678", 0.93);
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 2500");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_deposit_more_than_twenty_five_hundred_dollars_into_savings() {
        bank.openSavingsAccount("12345678", 0.63);
        String[] commandArgs = commandInput.commandToArray("deposit 87654321 2501");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void deposit_zero_dollars_into_checking_account_is_valid() {
        bank.openCheckingAccount("12345678", 0.57);
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 0");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void deposit_one_dollar_into_checking_account_is_valid() {
        bank.openCheckingAccount("12345678", 0.89);
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 1");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_deposit_negative_dollars_into_checking_account() {
        bank.openCheckingAccount("12345678", 0.29);
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 -1");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void able_to_deposit_one_thousand_dollars_into_checking_account() {
        bank.openCheckingAccount("12345678", 0.21);
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 1000");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_deposit_more_than_thousand_dollars_into_checking_account() {
        bank.openCheckingAccount("12345678", 0.91);
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 1001");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void deposit_amount_has_unexpected_symbol() {
        bank.openSavingsAccount("12345678", 0.01);
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 1,000");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void invalid_deposit_amount_is_not_a_double() {
        bank.openSavingsAccount("12345678", 0.01);
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 h");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void invalid_deposit_amount_contains_letters() {
        bank.openSavingsAccount("12345678", 0.01);
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 1ab");
        assertFalse(commandValidator.validate(commandArgs));
    }



}
