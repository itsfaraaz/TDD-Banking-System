package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandValidatorTest {
    CommandValidator commandValidator;
    CommandInput commandInput;
    Bank bank;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);
        commandInput = new CommandInput();
    }

    @Test
    public void valid_command_type_of_create() {
        String[] commandArr = commandInput.commandToArray("create checking 12345678 0.1");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void command_type_of_create_uses_all_caps_is_valid() {
        String[] commandArr = commandInput.commandToArray("CREATE checking 12345678 0.1");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void command_type_of_create_misspelled_is_invalid() {
        String[] commandArr = commandInput.commandToArray("creat checking 12345678 0.1");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void command_type_of_create_has_numbers_is_invalid() {
        String[] commandArr = commandInput.commandToArray("creat45e checking 12345678 0.1");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void valid_command_type_of_deposit() {
        bank.openCheckingAccount("87654321", 0.1);
        String[] commandArr = commandInput.commandToArray("deposit 87654321 500");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void command_type_of_deposit_uses_all_caps_is_valid() {
        bank.openCheckingAccount("87654321", 0.1);
        String[] commandArr = commandInput.commandToArray("DEPOSIT 87654321 500");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void command_type_of_deposit_misspelled_is_invalid() {
        String[] commandArr = commandInput.commandToArray("depsoit 87654321 1250");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void command_type_of_deposit_has_numbers_is_invalid() {
        String[] commandArr = commandInput.commandToArray("de98posit 87654321 1250");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void valid_command_type_for_withdraw() {
        bank.openCheckingAccount("12345678", 0.1);
        String[] commandArr = commandInput.commandToArray("withdraw 12345678 300");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void command_type_of_withdraw_uses_all_caps_is_valid() {
        bank.openCheckingAccount("12345678", 0.1);
        String[] commandArr = commandInput.commandToArray("WITHDRAW 12345678 300");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void command_type_of_withdraw_misspelled_is_invalid() {
        String[] commandArr = commandInput.commandToArray("withdrawer 12345678 300");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void command_type_of_withdraw_has_numbers_is_invalid() {
        String[] commandArr = commandInput.commandToArray("w1thdr4w 12345678 300");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void valid_command_type_for_transfer() {
        bank.openCheckingAccount("12345678", 0.1);
        bank.openCheckingAccount("87654321", 0.1);
        String[] commandArr = commandInput.commandToArray("transfer 12345678 87654321 300");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void command_type_of_transfer_uses_all_caps_is_valid() {
        bank.openCheckingAccount("12345678", 0.1);
        bank.openCheckingAccount("87654321", 0.1);
        String[] commandArr = commandInput.commandToArray("TRANSFER 12345678 87654321 300");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void command_type_of_transfer_misspelled_is_invalid() {
        String[] commandArr = commandInput.commandToArray("tansfer 12345678 300");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void command_type_of_transfer_has_numbers_is_invalid() {
        String[] commandArr = commandInput.commandToArray("tr4n5f3r 12345678 300");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void valid_command_type_for_pass_time() {
        String[] commandArr = commandInput.commandToArray("pass 1");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void command_type_for_pass_time_uses_all_caps_is_valid() {
        String[] commandArr = commandInput.commandToArray("PASS 1");
        boolean actual = commandValidator.validate(commandArr);
        assertTrue(actual);
    }

    @Test
    public void command_type_of_pass_time_misspelled_is_invalid() {
        String[] commandArr = commandInput.commandToArray("pas 1");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }

    @Test
    public void command_type_of_pass_time_has_numbers_is_invalid() {
        String[] commandArr = commandInput.commandToArray("p45s 1");
        boolean actual = commandValidator.validate(commandArr);
        assertFalse(actual);
    }
}
