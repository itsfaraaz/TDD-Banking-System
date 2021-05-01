package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static banking.BankTest.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransferCommandValidatorTest {

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
    void missing_account_to_transfer_from_and_to() {
        String[] commandArgs = commandInput.commandToArray("transfer");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void missing_account_to_transfer_to() {
        String[] commandArgs = commandInput.commandToArray("transfer 12345678");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void missing_amount_to_transfer() {
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 87654321");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_make_transfer_if_FROM_account_ID_doesnt_exist() {
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 87654321 300");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_make_transfer_if_TO_account_ID_doesnt_exist() {
        bank.openCheckingAccount(ID, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 87654321 300");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_make_transfer_if_both_account_ID_doesnt_exist() {
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 87654321 300");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_make_a_transfer_to_the_same_account() {
        bank.openCheckingAccount(ID, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345678 300");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void can_transfer_zero_dollars_between_two_checking_accounts() {
        bank.openCheckingAccount(ID, APR);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 0");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void can_transfer_one_dollar_between_two_checking_accounts() {
        bank.openCheckingAccount(ID, APR);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 1");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_transfer_negative_amounts_between_checking_accounts() {
        bank.openCheckingAccount(ID, APR);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 -1");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void can_transfer_max_four_hundred_dollars_between_checking_accounts() {
        bank.openCheckingAccount(ID, APR);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 400");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_transfer_more_than_four_hundred_dollars_between_checking_accounts() {
        bank.openCheckingAccount(ID, APR);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 401");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void can_transfer_zero_dollars_between_two_savings_accounts() {
        bank.openSavingsAccount(ID, APR);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 0");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void can_transfer_one_dollar_between_two_savings_accounts() {
        bank.openSavingsAccount(ID, APR);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 1");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_transfer_negative_amount_between_two_savings_accounts() {
        bank.openSavingsAccount(ID, APR);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 -1");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void can_transfer_max_thousand_dollars_between_savings() {
        bank.openSavingsAccount(ID, APR);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 1000");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_transfer_more_than_one_thousand_dollars_between_two_saving_accounts() {
        bank.openSavingsAccount(ID, APR);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 1001");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void can_transfer_zero_dollars_from_checking_to_savings() {
        bank.openCheckingAccount(ID, APR);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 0");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void can_transfer_one_dollar_from_checking_to_savings() {
        bank.openCheckingAccount(ID, APR);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 1");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_transfer_negative_amount_from_checking_to_savings() {
        bank.openCheckingAccount(ID, APR);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 -1");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void can_transfer_four_hundred_dollars_between_checking_and_savings() {
        bank.openCheckingAccount(ID, APR);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 400");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_transfer_more_than_four_hundred_dollars_from_checking_account() {
        bank.openCheckingAccount(ID, APR);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 401");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_transfer_twice_between_savings_account_due_to_limit() {
        bank.openSavingsAccount(ID, APR);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 400");
        assertTrue(commandValidator.validate(commandArgs));
        commandArgs = commandInput.commandToArray("transfer 12345678 87654321");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void can_transfer_zero_dollars_from_savings_to_checking() {
        bank.openSavingsAccount(ID, APR);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 0");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void can_transfer_one_dollar_between_accounts() {
        bank.openSavingsAccount(ID, APR);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 1");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_transfer_negative_amount() {
        bank.openSavingsAccount(ID, APR);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 -1");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void can_transfer_a_thousand_dollars_to_checking_account() {
        bank.openSavingsAccount(ID, APR);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 1000");
        assertTrue(commandValidator.validate(commandArgs));
    }

    @Test
    void cannot_transfer_from_or_to_cd_account() {
        bank.openCheckingAccount(ID, APR);
        bank.openCDAccount(ID_DIFFERENT, APR, 5000);
        String[] commandArgs = commandInput.commandToArray("transfer 87654321 12345677 3000");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void transfer_amount_has_unexpected_symbol() {
        bank.openSavingsAccount(ID, APR);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 1,000");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void transfer_amount_is_not_a_double() {
        bank.openSavingsAccount(ID, APR);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 abc");
        assertFalse(commandValidator.validate(commandArgs));
    }

    @Test
    void transfer_amount_has_unexpected_characters() {
        bank.openSavingsAccount(ID, APR);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 1aa");
        assertFalse(commandValidator.validate(commandArgs));
    }

}
