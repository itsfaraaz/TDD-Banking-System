package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static banking.BankTest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CommandProcessorTest {
    private CommandProcessor processor;
    private Bank bank;
    private CommandInput commandInput;

    @BeforeEach
    void setup() {
        bank = new Bank();
        processor = new CommandProcessor(bank);
        commandInput = new CommandInput();
    }

    @Test
    void open_checking_account() {
        String[] commandArgs = commandInput.commandToArray("create checking 12345678 0.01");
        processor.execute(commandArgs);
        assertEquals(ID, bank.getAccounts().get(ID).getID());
        assertEquals(APR, bank.getAccounts().get(ID).getAPR());
    }

    @Test
    void open_savings_account() {
        String[] commandArgs = commandInput.commandToArray("create savings 12345677 0.01");
        processor.execute(commandArgs);
        assertEquals(ID_DIFFERENT, bank.getAccounts().get(ID_DIFFERENT).getID());
        assertEquals(APR, bank.getAccounts().get(ID_DIFFERENT).getAPR());
    }

    @Test
    void open_cd_account() {
        String[] commandArgs = commandInput.commandToArray("create cd 12345678 0.01 2500");
        processor.execute(commandArgs);
        assertEquals(ID, bank.getAccounts().get(ID).getID());
        assertEquals(APR, bank.getAccounts().get(ID).getAPR());
        assertEquals(2500, bank.getAccounts().get(ID).getBalance());
    }

    @Test
    void deposit_into_empty_checking_account() {
        bank.openCheckingAccount(ID, APR);
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 1000");
        processor.execute(commandArgs);
        assertEquals(1000, bank.getAccounts().get(ID).getBalance());
    }

    @Test
    void deposit_into_checking_with_balance_greater_than_zero() {
        bank.openCheckingAccount(ID, APR);
        bank.depositInto(ID, 1000);
        double initialBalance = bank.getAccounts().get(ID).getBalance();
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 1000");
        processor.execute(commandArgs);
        assertEquals(initialBalance * 2, bank.getAccounts().get(ID).getBalance());
    }

    @Test
    void deposit_into_empty_savings_account() {
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("deposit 12345677 1000");
        processor.execute(commandArgs);
        assertEquals(1000, bank.getAccounts().get(ID_DIFFERENT).getBalance());
    }

    @Test
    void deposit_into_savings_with_balance_greater_than_zero() {
        bank.openSavingsAccount(ID, APR);
        bank.depositInto(ID, 1000);
        double initialBalance = bank.getAccounts().get(ID).getBalance();
        String[] commandArgs = commandInput.commandToArray("deposit 12345678 1000");
        processor.execute(commandArgs);
        assertEquals(initialBalance * 2, bank.getAccounts().get(ID).getBalance());
    }

    @Test
    void deposit_into_checking_account_twice() {
        bank.openCheckingAccount(ID, APR);
        String[] firstCommandArgs = commandInput.commandToArray("deposit 12345678 1000");
        processor.execute(firstCommandArgs);
        String[] secondCommandArgs = commandInput.commandToArray("deposit 12345678 1000");
        processor.execute(secondCommandArgs);
        assertEquals(1000 * 2, bank.getAccounts().get(ID).getBalance());
    }

    @Test
    void withdraw_from_a_savings_account() {
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        bank.depositInto(ID_DIFFERENT, 100);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345677 50");
        processor.execute(commandArgs);
        assertEquals(100 - 50, bank.getAccounts().get(ID_DIFFERENT).getBalance());
    }

    @Test
    void withdraw_from_checking_account() {
        bank.openCheckingAccount(ID, APR);
        bank.depositInto(ID, 1000);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 500");
        processor.execute(commandArgs);
        assertEquals(1000 - 500, bank.getAccounts().get(ID).getBalance());
    }

    @Test
    void withdraw_from_a_cd_account() {
        bank.openCDAccount(ID, APR, 2500);
        String[] commandArgs = commandInput.commandToArray("withdraw 12345678 2500");
        processor.execute(commandArgs);
        assertEquals(0, bank.getAccounts().get(ID).getBalance());
    }

    @Test
    public void transfer_between_two_checking_accounts() {
        bank.openCheckingAccount(ID, APR);
        bank.depositInto(ID, 1000);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 200");
        processor.execute(commandArgs);
        assertEquals(800, bank.getAccounts().get(ID).getBalance());
        assertEquals(200, bank.getAccounts().get(ID_DIFFERENT).getBalance());
    }

    @Test
    public void transfer_between_two_savings_accounts() {
        bank.openSavingsAccount(ID, APR);
        bank.depositInto(ID, 2500);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 500");
        processor.execute(commandArgs);
        assertEquals(2000, bank.getAccounts().get(ID).getBalance());
        assertEquals(500, bank.getAccounts().get(ID_DIFFERENT).getBalance());
    }

    @Test
    public void transfer_from_checking_to_savings() {
        bank.openCheckingAccount(ID, APR);
        bank.depositInto(ID, 1000);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 400");
        processor.execute(commandArgs);
        assertEquals(600, bank.getAccounts().get(ID).getBalance());
        assertEquals(400, bank.getAccounts().get(ID_DIFFERENT).getBalance());
    }

    @Test
    public void transfer_from_savings_to_checking() {
        bank.openSavingsAccount(ID, APR);
        bank.depositInto(ID, 2500);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 1000");
        processor.execute(commandArgs);
        assertEquals(1500, bank.getAccounts().get(ID).getBalance());
        assertEquals(1000, bank.getAccounts().get(ID_DIFFERENT).getBalance());
    }

    @Test
    public void transfer_twice_between_accounts() {
        bank.openCheckingAccount(ID, APR);
        bank.depositInto(ID, 1000);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        String[] commandArgs = commandInput.commandToArray("transfer 12345678 12345677 400");
        processor.execute(commandArgs);
        commandArgs = commandInput.commandToArray("transfer 12345678 12345677 400");
        processor.execute(commandArgs);
        assertEquals(200, bank.getAccounts().get(ID).getBalance());
        assertEquals(800, bank.getAccounts().get(ID_DIFFERENT).getBalance());
    }

    @Test
    public void pass_time_one_month_when_balance_is_zero() {
        bank.openCheckingAccount(ID, 1.0);
        String[] commandArgs = commandInput.commandToArray("pass 1");
        processor.execute(commandArgs);
        assertNull(bank.getAccounts().get(ID));
    }

    @Test
    public void pass_time_one_month_when_balance_is_greater_than_zero_and_less_then_100() {
        bank.openCheckingAccount(ID, 1.0);
        bank.depositInto(ID, 55);
        String[] commandArgs = commandInput.commandToArray("pass 1");
        processor.execute(commandArgs);
        assertEquals(55.05, Math.round(bank.getAccounts().get(ID).getBalance() * 100.0) / 100.0);
    }

    @Test
    public void pass_time_one_month_when_balance_is_greater_than_100() {
        bank.openCheckingAccount(ID, 1.0);
        bank.depositInto(ID, 5000);
        String[] commandArgs = commandInput.commandToArray("pass 1");
        processor.execute(commandArgs);
        assertEquals(5004.17, Math.round(bank.getAccounts().get(ID).getBalance() * 100.0) / 100.0);
    }

    @Test
    void pass_time_one_month_when_cd_account_present() {
        bank.openCDAccount(ID, 1.0, 5000);
        String[] commandArgs = commandInput.commandToArray("pass 1");
        processor.execute(commandArgs);
        assertEquals(5016.69, Math.round(bank.getAccounts().get(ID).getBalance() * 100.0) / 100.0);
    }

    @Test
    void pass_time_twice() {
        bank.openCheckingAccount(ID, 1.0);
        bank.depositInto(ID, 125);
        String[] commandArgs = commandInput.commandToArray("pass 3");
        processor.execute(commandArgs);
        assertEquals(125.31, Math.round(bank.getAccounts().get(ID).getBalance() * 100.0) / 100.0);
        commandArgs = commandInput.commandToArray("pass 2");
        processor.execute(commandArgs);
        assertEquals(125.52, Math.round(bank.getAccounts().get(ID).getBalance() * 100.0) / 100.0);
    }

}
