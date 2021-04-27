package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateCommandValidatorTest {
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
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void invalid_command_missing_account_type_id_and_apr() {
        String[] commandArgs = commandInput.commandToArray("create");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void invalid_command_missing_id_and_apr() {
        String[] commandArgs = commandInput.commandToArray("create checking");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void invalid_command_missing_apr() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void invalid_command_missing_starting_balance_for_cd_account() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.1");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void account_type_is_all_caps_is_valid() {
        String[] commandArgs = commandInput.commandToArray("create CHECKING 12345678 0.1");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertTrue(isCommandValid);
    }

    @Test
    void id_is_less_than_eight_digits_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create checking 1234567 0.1");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void id_is_greater_than_eight_digits_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create checking 123456789 0.1");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void id_is_not_an_eight_digit_number_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create checking 123abc789 0.1");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void duplicate_checking_id_is_invalid() {
        bank.openCheckingAccount("12345678", 0.1);
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 0.1");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void new_checking_id_is_valid() {
        bank.openCheckingAccount("12345678", 0.1);
        String[] commandArgs = commandInput.commandToArray("create checking 13345678 0.1");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertTrue(isCommandValid);
    }

    @Test
    void duplicate_savings_id_is_invalid() {
        bank.openSavingsAccount("12345678", 0.1);
        String[] commandArgs = commandInput.commandToArray("create savings 12345678 0.1");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void new_savings_id_is_valid() {
        bank.openSavingsAccount("12345678", 0.1);
        String[] commandArgs = commandInput.commandToArray("create savings 12345638 0.1");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertTrue(isCommandValid);
    }

    @Test
    void duplicate_cd_id_is_invalid() {
        bank.openCDAccount("12345678", 0.01, 1000);
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.1 1000");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void new_cd_id_is_valid() {
        bank.openCDAccount("12345678", 0.01, 1000);
        String[] commandArgs = commandInput.commandToArray("create cd 12345378 0.1 1000");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertTrue(isCommandValid);
    }

    @Test
    void apr_is_zero_is_valid() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 0");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertTrue(isCommandValid);
    }

    @Test
    void apr_is_one_is_valid() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 1");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertTrue(isCommandValid);
    }

    @Test
    void apr_is_ten_is_valid() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 10");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertTrue(isCommandValid);
    }

    @Test
    void apr_is_eleven_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 11");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void apr_is_negative_value_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 -1");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void apr_is_one_thousand_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 1000");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void apr_is_not_a_double_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 0.a");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void amount_for_cd_is_zero_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.99 0");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void amount_for_cd_is_one_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 1 1");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void amount_for_cd_is_999_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.39 999");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void amount_for_cd_is_one_thousand_is_valid() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.23 1000");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertTrue(isCommandValid);
    }

    @Test
    void amount_for_cd_is_ten_thousand_is_valid() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.99 10000");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertTrue(isCommandValid);
    }

    @Test
    void amount_for_cd_is_10001_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 1.04 10001");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void amount_for_cd_is_a_negative_value_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.83 -1");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void amount_for_cd_is_hundred_thousand_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.87 100000");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void amount_for_cd_has_letters_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.21 6b");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

    @Test
    void account_type_is_misspelled_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create save 12345678 0.01");
        boolean isCommandValid = commandValidator.validate(commandArgs);
        assertFalse(isCommandValid);
    }

//    @Test
//    void command_is_trying_to_create_cd() {
//        String[] commandArgs = commandInput.commandToArray("create cd 13579135 1.00 2000");
//        CommandValidator validator = new CommandValidator(bank);
//        boolean isCommandValid = validator.validationIfCd(commandArgs);
//        assertTrue(isCommandValid);
//    }
}
