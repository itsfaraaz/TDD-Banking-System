package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static banking.AccountTest.BALANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankTest {

    static String ID = "12345678";
    static String ID_DIFFERENT = "12345677";
    static double APR = 0.01;
    static double AMOUNT = 1000;
    private Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
    }

    @Test
    void bank_initially_has_no_accounts() {
        assertTrue(bank.getAccounts().isEmpty());
    }

    @Test
    void open_a_checking_account() {
        bank.openCheckingAccount(ID, APR);
        assertEquals(0, bank.getAccounts().get(ID).getBalance());
    }

    @Test
    void open_a_savings_account() {
        bank.openSavingsAccount(ID, APR);
        assertEquals(0, bank.getAccounts().get(ID).getBalance());
    }

    @Test
    void open_a_cd_account() {
        bank.openCDAccount(ID, APR, BALANCE);
        assertEquals(1000, bank.getAccounts().get(ID).getBalance());
    }

    // EDIT AFTER

    @Test
    void open_multiple_bank_accounts() {
        bank.openCheckingAccount(ID, APR);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        assertEquals(0, bank.getAccounts().get(ID_DIFFERENT).getBalance());
    }

    @Test
    void deposit_into_an_account() {
        bank.openCheckingAccount(ID, APR);
        bank.depositInto(ID, AMOUNT);
        Account account = bank.getAccounts().get(ID);
        assertEquals(AMOUNT, account.getBalance());
    }

    @Test
    void deposit_into_an_account_twice() {
        bank.openSavingsAccount(ID, APR);
        bank.depositInto(ID, AMOUNT);
        bank.depositInto(ID, 1);
        Account account = bank.getAccounts().get(ID);
        assertEquals(AMOUNT + 1, account.getBalance());
    }

    @Test
    void withdraw_from_an_account() {
        bank.openCheckingAccount(ID, APR);
        bank.depositInto(ID, AMOUNT * 2);
        bank.withdrawFrom(ID, AMOUNT);
        Account account = bank.getAccounts().get(ID);
        assertEquals((AMOUNT * 2) - AMOUNT, account.getBalance());
    }

    @Test
    void withdraw_twice_from_an_account() {
        bank.openSavingsAccount(ID, APR);
        bank.depositInto(ID, AMOUNT + 1);
        bank.withdrawFrom(ID, AMOUNT);
        bank.withdrawFrom(ID, 1);
        Account account = bank.getAccounts().get(ID);
        assertEquals((AMOUNT + 1) - (AMOUNT + 1), account.getBalance());
    }

    @Test
    void withdraw_more_than_account_balance() {
        bank.openCheckingAccount(ID, APR);
        bank.depositInto(ID, AMOUNT);
        bank.withdrawFrom(ID, AMOUNT + 1);
        Account account = bank.getAccounts().get(ID);
        assertEquals(0, account.getBalance());
    }

    @Test
    void transfer_between_two_checking_accounts() {
        bank.openCheckingAccount(ID, APR);
        bank.depositInto(ID, AMOUNT);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        bank.transferBetweenAccounts(ID, ID_DIFFERENT, AMOUNT);
        Account firstAccount = bank.getAccounts().get(ID);
        Account secondAccount = bank.getAccounts().get(ID_DIFFERENT);
        assertEquals(0, firstAccount.getBalance());
        assertEquals(1000, secondAccount.getBalance());
    }

    @Test
    void transfer_between_two_savings_accounts() {
        bank.openSavingsAccount(ID, APR);
        bank.depositInto(ID, 5000);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        bank.transferBetweenAccounts(ID, ID_DIFFERENT, 5000);
        Account firstAccount = bank.getAccounts().get(ID);
        Account seccondAccount = bank.getAccounts().get(ID_DIFFERENT);
        assertEquals(0, firstAccount.getBalance());
        assertEquals(5000, seccondAccount.getBalance());
    }

    @Test
    void transfer_from_checking_to_savings_account() {
        bank.openCheckingAccount(ID, APR);
        bank.depositInto(ID, AMOUNT);
        bank.openSavingsAccount(ID_DIFFERENT, APR);
        bank.transferBetweenAccounts(ID, ID_DIFFERENT, AMOUNT);
        Account checkingAccount = bank.getAccounts().get(ID);
        Account savingsAccount = bank.getAccounts().get(ID_DIFFERENT);
        assertEquals(0, checkingAccount.getBalance());
        assertEquals(1000, savingsAccount.getBalance());
    }

    @Test
    void multiple_transfers_between_checking_and_savings() {
        bank.openSavingsAccount(ID, APR);
        bank.depositInto(ID, 5000);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        bank.transferBetweenAccounts(ID, ID_DIFFERENT, 1000);
        Account savingsAccount = bank.getAccounts().get(ID);
        Account checkingAccount = bank.getAccounts().get(ID_DIFFERENT);
        assertEquals(4000, savingsAccount.getBalance());
        assertEquals(1000, checkingAccount.getBalance());
        bank.transferBetweenAccounts(ID, ID_DIFFERENT, 2000);
        assertEquals(2000, savingsAccount.getBalance());
        assertEquals(3000, checkingAccount.getBalance());
    }

    @Test
    void transfer_amount_greater_than_accounts_current_balance() {
        bank.openCheckingAccount(ID, APR);
        bank.depositInto(ID, 500);
        bank.openCheckingAccount(ID_DIFFERENT, APR);
        bank.transferBetweenAccounts(ID, ID_DIFFERENT, 1000);
        Account firstAccount = bank.getAccounts().get(ID);
        Account secondAccount = bank.getAccounts().get(ID_DIFFERENT);
        assertEquals(0, firstAccount.getBalance());
        assertEquals(500, secondAccount.getBalance());
    }

    // WRITE PASS TIME TESTS


}
