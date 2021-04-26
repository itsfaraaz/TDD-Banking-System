package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class CreateCommandValidatorTest {
    CommandValidator commandValidator;
    private Bank bank;
    private CommandInput commandInput;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);
        commandInput = new CommandInput();
    }

    @Test
    public void empty_input_command_is_invalid() {
        String[] commandArr = commandInput.commandToArray("");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void missing_account_type_id_and_apr_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void missing_id_and_apr_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create checking");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void missing_apr_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create checking 12345678");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void missing_amount_for_cd_account_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create cd 12345678 0.1");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void account_type_is_all_caps_is_valid() {
        String[] commandArr = commandInput.commandToArray("create CHECKING 12345678 0.1");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void id_is_less_than_eight_digits_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create checking 1234567 0.1");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void id_is_greater_than_eight_digits_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create checking 123456789 0.1");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void id_is_not_an_eight_digit_number_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create checking 123abc789 0.1");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void duplicate_checking_id_is_invalid() {
        bank.openCheckingAccount("12345678", 0.1);
        String[] commandArr = commandInput.commandToArray("create checking 12345678 0.1");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void new_checking_id_is_valid() {
        bank.addCheckingAccount("12345678", 0.1);
        String[] commandArr = commandInput.commandToArray("create checking 23456789 0.1");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void duplicate_savings_id_is_invalid() {
        bank.addSavingsAccount("87654321", 0.1);
        String[] commandArr = commandInput.commandToArray("create savings 87654321 0.1");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void new_savings_id_is_valid() {
        bank.addSavingsAccount("87654321", 0.1);
        String[] commandArr = commandInput.commandToArray("create savings 98765432 0.1");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void duplicate_cd_id_is_invalid() {
        bank.addCdAccount("13579135", 0.1, 2000);
        String[] commandArr = commandInput.commandToArray("create cd 13579135 0.1 2000");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void new_cd_id_is_valid() {
        bank.addCdAccount("13579135", 0.1, 2000);
        String[] commandArr = commandInput.commandToArray("create cd 53197531 0.1 2000");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void apr_is_zero_is_valid() {
        String[] commandArr = commandInput.commandToArray("create checking 12345678 0");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void apr_is_one_is_valid() {
        String[] commandArr = commandInput.commandToArray("create checking 12345678 1");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void apr_is_ten_is_valid() {
        String[] commandArr = commandInput.commandToArray("create checking 12345678 10");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void apr_is_eleven_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create checking 12345678 11");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void apr_is_negative_value_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create checking 12345678 -1");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void apr_is_one_thousand_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create checking 12345678 1000");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void apr_is_not_a_double_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create checking 12345678 0.a");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void amount_for_cd_is_zero_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create cd 13579135 1 0");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void amount_for_cd_is_one_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create cd 13579135 1 1");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void amount_for_cd_is_999_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create cd 13579135 1 999");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void amount_for_cd_is_one_thousand_is_valid() {
        String[] commandArr = commandInput.commandToArray("create cd 13579135 1 1000");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void amount_for_cd_is_ten_thousand_is_valid() {
        String[] commandArr = commandInput.commandToArray("create cd 13579135 1 10000");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void amount_for_cd_is_10001_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create cd 13579135 1 10001");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void amount_for_cd_is_a_negative_value_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create cd 13579135 1 -1");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    void amount_for_cd_is_hundred_thousand_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create cd 13579135 1 100000");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void amount_for_cd_has_letters_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create cd 13579135 1 9bf");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void account_type_is_misspelled_is_invalid() {
        String[] commandArr = commandInput.commandToArray("create save 87654321 0.21");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void command_is_trying_to_create_cd() {
        String[] commandArr = commandInput.commandToArray("create cd 13579135 1.00 2000");
        commandValidator validator = new commandValidator(bank);
        boolean actual = validator.validationIfCd(commandArr);
        assertTrue(actual);
    }
}
