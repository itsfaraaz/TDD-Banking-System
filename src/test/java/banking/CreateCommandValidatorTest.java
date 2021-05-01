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
    void empty_command_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_an_account_without_the_account_type_accountID_and_apr() {
        String[] commandArgs = commandInput.commandToArray("create");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_an_account_without_an_accountID_and_apr() {
        String[] commandArgs = commandInput.commandToArray("create checking");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_an_account_without_an_apr() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_cd_account_without_a_starting_balance() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.1");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void can_create_account_with_checking_in_all_caps() {
        String[] commandArgs = commandInput.commandToArray("create CHECKING 12345678 0.1");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_account_with_less_than_eight_numbers() {
        String[] commandArgs = commandInput.commandToArray("create checking 1234567 0.1");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_account_with_more_than_eight_numbers() {
        String[] commandArgs = commandInput.commandToArray("create checking 123456789 0.1");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_an_account_with_letters_in_ID() {
        String[] commandArgs = commandInput.commandToArray("create checking 123abc789 0.1");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_a_checking_account_with_existing_account_ID() {
        bank.openCheckingAccount("12345678", 0.1);
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 0.1");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void can_create_a_checking_account_with_any_combination_of_numbers() {
        bank.openCheckingAccount("12345678", 0.1);
        String[] commandArgs = commandInput.commandToArray("create checking 13345678 0.1");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_a_savings_account_with_existing_account_ID() {
        bank.openSavingsAccount("12345678", 0.1);
        String[] commandArgs = commandInput.commandToArray("create savings 12345678 0.1");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void can_create_a_savings_account_with_any_combination_of_numbers() {
        bank.openSavingsAccount("12345678", 0.1);
        String[] commandArgs = commandInput.commandToArray("create savings 12345638 0.1");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_a_cd_account_with_existing_account_ID() {
        bank.openCDAccount("12345678", 0.01, 1000);
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.1 1000");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void can_create_a_cd_account_with_any_combination_of_numbers() {
        bank.openCDAccount("12345678", 0.01, 1000);
        String[] commandArgs = commandInput.commandToArray("create cd 12345677 0.1 1000");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void can_create_an_account_with_zero_apr() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 0");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void can_create_an_account_with_a_whole_number_instead_of_decimal() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 1");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void can_create_an_account_with_max_ten_percent() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 10");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_account_with_a_negative_apr() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 -1");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_an_account_with_an_apr_over_ten_percent() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 11");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void unexpected_character_in_apr_which_is_not_a_double_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 0.a");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_cd_account_with_empty_starting_balance() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.99 0");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_cd_account_with_less_than_one_thousand_starting_balance() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.39 999");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void can_create_cd_account_within_minimum_balance_of_one_thousand() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.23 1000");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void can_create_cd_account_with_max_balance_of_ten_thousand() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.93 10000");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_cd_account_with_a_balance_over_ten_thousand() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 1.09 10001");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_create_cd_account_with_a_negative_start_balance() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 2.83 -1");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void unexpected_letters_in_starting_balance_for_cd_account() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 1.21 6b");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void typo_in_command_type_is_invalid() {
        String[] commandArgs = commandInput.commandToArray("create save 12345678 0.01");
        assertFalse(commandValidator.validate(commandArgs));
    }
}
