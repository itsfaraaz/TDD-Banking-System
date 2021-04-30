package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandValidatorTest {
    private CommandValidator commandValidator;
    private CommandInput commandInput;
    private Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);
        commandInput = new CommandInput();
    }

    @Test
    void validate_a_create_command() {
        String[] commandArr = commandInput.commandToArray("create checking 12345678 0.1");
        assertTrue(commandValidator.validate(commandArr));
    }

    @Test
    void create_in_all_caps_is_a_valid_command() {
        String[] commandArr = commandInput.commandToArray("CREATE checking 12345678 0.1");
        assertTrue(commandValidator.validate(commandArr));
    }

    @Test
    void typo_in_create_is_an_invalid_command() {
        String[] commandArr = commandInput.commandToArray("creat checking 12345678 0.1");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void create_has_unexpected_numbers_which_makes_command_invalid() {
        String[] commandArr = commandInput.commandToArray("crea3te checking 12345678 0.1");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void validate_a_deposit_command() {
        bank.openCheckingAccount("12345678", 0.1);
        String[] commandArr = commandInput.commandToArray("deposit 12345678 250");
        assertTrue(commandValidator.validate(commandArr));
    }

    @Test
    void deposit_in_all_caps_is_valid() {
        bank.openCheckingAccount("12345678", 0.1);
        String[] commandArr = commandInput.commandToArray("DEPOSIT 12345678 500");
        assertTrue(commandValidator.validate(commandArr));
    }

    @Test
    void typo_in_deposit_command_is_invalid() {
        String[] commandArr = commandInput.commandToArray("depsoit 12345678 500");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void unexpcted_number_in_deposit_is_invalid() {
        String[] commandArr = commandInput.commandToArray("deposit3 12345678 500");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void validate_a_withdraw_command() {
        bank.openCheckingAccount("12345678", 0.1);
        String[] commandArr = commandInput.commandToArray("withdraw 12345678 400");
        assertTrue(commandValidator.validate(commandArr));
    }

    @Test
    void withdraw_in_all_caps_is_valid() {
        bank.openCheckingAccount("12345678", 0.1);
        String[] commandArr = commandInput.commandToArray("WITHDRAW 12345678 400");
        assertTrue(commandValidator.validate(commandArr));
    }

    @Test
    void typo_in_withdraw_is_invalid() {
        String[] commandArr = commandInput.commandToArray("withdraer 12345678 500");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void unexpcted_numbers_in_withdraw_command_is_invalid() {
        String[] commandArr = commandInput.commandToArray("withdra22w 12345678 500");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void validate_a_transfer_command() {
        bank.openCheckingAccount("12345678", 0.1);
        bank.depositInto("12345678", 500);
        bank.openCheckingAccount("87654321", 0.1);
        String[] commandArr = commandInput.commandToArray("transfer 12345678 87654321 300");
        assertTrue(commandValidator.validate(commandArr));
    }

    @Test
    void transfer_in_all_caps_is_valid() {
        bank.openCheckingAccount("12345678", 0.1);
        bank.depositInto("12345678", 500);
        bank.openSavingsAccount("12345677", 0.1);
        String[] commandArr = commandInput.commandToArray("TRANSFER 12345678 12345677 100");
        assertTrue(commandValidator.validate(commandArr));
    }

    @Test
    void typo_in_transfer_is_invalid() {
        bank.openCheckingAccount("12345678", 0.1);
        bank.depositInto("12345678", 500);
        bank.openSavingsAccount("12345677", 0.1);
        String[] commandArr = commandInput.commandToArray("tansfer 12345678 12345677 300");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void unexpected_numbers_in_transfer_command_is_invalid() {
        bank.openCheckingAccount("12345678", 0.1);
        bank.depositInto("12345678", 500);
        bank.openSavingsAccount("12345677", 0.1);
        String[] commandArr = commandInput.commandToArray("tr4n5f3r 12345678 12345674 300");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void validate_a_pass_time_command() {
        String[] commandArr = commandInput.commandToArray("pass 1");
        assertTrue(commandValidator.validate(commandArr));
    }

    @Test
    void pass_in_all_caps_is_valid() {
        String[] commandArr = commandInput.commandToArray("PASS 1");
        assertTrue(commandValidator.validate(commandArr));
    }

    @Test
    void typo_in_pass_is_invalid() {
        String[] commandArr = commandInput.commandToArray("pas 1");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void unexpected_numbers_in_pass_is_invalid() {
        String[] commandArr = commandInput.commandToArray("pas3s 1");
        assertFalse(commandValidator.validate(commandArr));
    }
}
