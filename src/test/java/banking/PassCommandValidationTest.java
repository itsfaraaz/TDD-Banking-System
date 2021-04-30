package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PassCommandValidationTest {

    private CommandValidator commandValidator;
    private CommandInput commandInput;
    private Bank bank;

    @BeforeEach
    void setup() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);
        commandInput = new CommandInput();
    }

    @Test
    void invalid_empty_command() {
        String[] commandArr = commandInput.commandToArray("");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void invalid_command_as_no_month_is_given() {
        String[] commandArr = commandInput.commandToArray("pass");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void given_month_must_be_at_least_one() {
        String[] commandArr = commandInput.commandToArray("pass 0");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void given_month_is_at_least_one_is_valid() {
        String[] commandArr = commandInput.commandToArray("pass 1");
        assertTrue(commandValidator.validate(commandArr));
    }

    @Test
    void month_cannot_be_negative() {
        String[] commandArr = commandInput.commandToArray("pass -1");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void month_can_be_max_sixty_months() {
        String[] commandArr = commandInput.commandToArray("pass 60");
        assertTrue(commandValidator.validate(commandArr));
    }

    @Test
    void months_cannot_be_more_than_sixty() {
        String[] commandArr = commandInput.commandToArray("pass 61");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void unexpected_symbol_in_month_argument() {
        String[] commandArr = commandInput.commandToArray("pass 1,00");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void month_is_not_a_valid_integer() {
        String[] commandArr = commandInput.commandToArray("pass kfd");
        assertFalse(commandValidator.validate(commandArr));
    }

    @Test
    void months_has_letters_in_it_is_invalid() {
        String[] commandArr = commandInput.commandToArray("pass 2d");
        assertFalse(commandValidator.validate(commandArr));
    }

}
