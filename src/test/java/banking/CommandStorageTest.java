package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandStorageTest {
    private CommandStorage commandStorage;
    private Bank bank;

    private final String INVALID_CREATE_COMMAND = "creat savings 12345678 0.01";
    private final String INVALID_DEPOSIT_COMMAND = "deposit savings 124345678 0.01";

    @BeforeEach
    void setup() {
        bank = new Bank();
        commandStorage = new CommandStorage(bank);
    }

    @Test
    void invalid_commands_in_storage_is_empty() {
        assertEquals(0, commandStorage.getInvalidCommands().size());
    }

    @Test
    void store_invalid_command() {
        commandStorage.storeInvalidCommand(INVALID_CREATE_COMMAND);
        assertEquals(INVALID_CREATE_COMMAND, commandStorage.getInvalidCommands().get(0));
    }

    @Test
    void store_two_invalid_commands() {
        commandStorage.storeInvalidCommand(INVALID_CREATE_COMMAND);
        commandStorage.storeInvalidCommand(INVALID_DEPOSIT_COMMAND);
        assertEquals(INVALID_CREATE_COMMAND, commandStorage.getInvalidCommands().get(0));
        assertEquals(INVALID_DEPOSIT_COMMAND, commandStorage.getInvalidCommands().get(1));
    }

    @Test
    void only_one_invalid_command_is_currently_stored() {
        commandStorage.storeInvalidCommand(INVALID_CREATE_COMMAND);
        assertEquals(1, commandStorage.getInvalidCommands().size());
    }

    @Test
    void two_invalid_commands_currently_stored() {
        commandStorage.storeInvalidCommand(INVALID_CREATE_COMMAND);
        commandStorage.storeInvalidCommand(INVALID_DEPOSIT_COMMAND);
        assertEquals(2, commandStorage.getInvalidCommands().size());
    }

    @Test
    void initial_transaction_history_is_empty() {
        assertEquals(0, commandStorage.getValidCommands().size());
    }

    @Test
    void store_a_valid_command_for_an_ids_transaction_history() {
        bank.openSavingsAccount("12345678", 0.6);
        commandStorage.storeValidCommand("Deposit 12345678 700");
        assertEquals("Deposit 12345678 700", commandStorage.getValidCommands().get("12345678").get(0));
    }

    @Test
    void store_two_valid_commands_for_an_ids_transaction_history() {
        bank.openSavingsAccount("12345678", 0.6);
        bank.openCheckingAccount("87654321", 0.6);
        commandStorage.storeValidCommand("Deposit 12345678 700");
        commandStorage.storeValidCommand("Transfer 87654321 12345678 300");
        assertEquals("Deposit 12345678 700", commandStorage.getValidCommands().get("12345678").get(0));
        assertEquals("Transfer 87654321 12345678 300", commandStorage.getValidCommands().get("12345678").get(1));
        assertEquals("Transfer 87654321 12345678 300", commandStorage.getValidCommands().get("87654321").get(0));
    }

    @Test
    void store_valid_commands_for_two_ids_transaction_history() {
        bank.openSavingsAccount("12345678", 0.6);
        bank.openCheckingAccount("98765432", 0.01);
        commandStorage.storeValidCommand("Deposit 12345678 700");
        commandStorage.storeValidCommand("DeposIt 98765432 200");
        assertEquals("Deposit 12345678 700", commandStorage.getValidCommands().get("12345678").get(0));
        assertEquals("DeposIt 98765432 200", commandStorage.getValidCommands().get("98765432").get(0));
    }

    @Test
    void initial_output_is_empty() {
        assertEquals(0, commandStorage.getOutput().size());
    }

    @Test
    void output_account_state() {
        bank.openSavingsAccount("12345678", 0.6);
        bank.depositInto("12345678", 1000.5023);
        assertEquals("Savings 12345678 1000.50 0.60", commandStorage.getOutput().get(0));
    }

    @Test
    void output_two_account_states() {
        bank.openSavingsAccount("12345678", 1.456);
        bank.depositInto("12345678", 13.43256);
        bank.openCheckingAccount("98765432", 0.323);
        bank.depositInto("98765432", 845.5);
        assertEquals("Savings 12345678 13.43 1.45", commandStorage.getOutput().get(0));
        assertEquals("Checking 98765432 845.50 0.32", commandStorage.getOutput().get(1));
    }

    @Test
    void output_account_state_with_one_transaction_history() {
        bank.openSavingsAccount("12345678", 0.6);
        bank.depositInto("12345678", 1000.059);
        commandStorage.storeValidCommand("Deposit 12345678 700");
        assertEquals("Savings 12345678 1000.05 0.60", commandStorage.getOutput().get(0));
        assertEquals("Deposit 12345678 700", commandStorage.getOutput().get(1));
    }

    @Test
    void output_account_state_with_two_transaction_history() {
        bank.openSavingsAccount("12345678", 0.6);
        bank.depositInto("12345678", 1000.059);
        bank.openCheckingAccount("87654321", 0.60);
        commandStorage.storeValidCommand("Deposit 12345678 700");
        commandStorage.storeValidCommand("trAnsFer 12345678 87654321 300");
        assertEquals("Savings 12345678 1000.05 0.60", commandStorage.getOutput().get(0));
        assertEquals("Deposit 12345678 700", commandStorage.getOutput().get(1));
        assertEquals("trAnsFer 12345678 87654321 300", commandStorage.getOutput().get(2));
        assertEquals("Checking 87654321 0.00 0.60", commandStorage.getOutput().get(3));
        assertEquals("trAnsFer 12345678 87654321 300", commandStorage.getOutput().get(4));
    }

    @Test
    void output_two_account_states_and_their_transaction_history() {
        bank.openSavingsAccount("12345678", 0.6);
        bank.depositInto("12345678", 1000.059);
        commandStorage.storeValidCommand("Deposit 12345678 700");
        bank.openCheckingAccount("98765432", 0.323);
        bank.depositInto("98765432", 845.5);
        commandStorage.storeValidCommand("depoSiT 98765432 45");
        assertEquals("Savings 12345678 1000.05 0.60", commandStorage.getOutput().get(0));
        assertEquals("Deposit 12345678 700", commandStorage.getOutput().get(1));
        assertEquals("Checking 98765432 845.50 0.32", commandStorage.getOutput().get(2));
        assertEquals("depoSiT 98765432 45", commandStorage.getOutput().get(3));
    }

    @Test
    void output_account_state_transaction_history_and_invalid_commands() {
        bank.openSavingsAccount("12345678", 0.6);
        bank.depositInto("12345678", 1000.059);
        commandStorage.storeInvalidCommand("depop 12345678 0");
        commandStorage.storeValidCommand("Deposit 12345678 700");
        assertEquals("Savings 12345678 1000.05 0.60", commandStorage.getOutput().get(0));
        assertEquals("Deposit 12345678 700", commandStorage.getOutput().get(1));
        assertEquals("depop 12345678 0", commandStorage.getOutput().get(2));
    }
}
