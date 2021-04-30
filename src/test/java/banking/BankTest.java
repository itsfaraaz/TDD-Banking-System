package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static banking.AccountTest.BALANCE;
import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    static final String ID = "12345678";
    static final String ID_DIFFERENT = "12345677";
    static final double APR = 0.01;
    private static final double APR_TWO = 0.60;
    static final double AMOUNT = 1000;
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

    @Test
    void pass_time_one_month_adds_one_month_to_current_month() {
        bank.openCheckingAccount(ID, APR);
        bank.depositInto(ID, 1000);
        bank.passTime(1);
        assertEquals(1, bank.getAccounts().get(ID).getMonths());
    }

    @Test
    void pass_time_by_12_months() {
        bank.openCheckingAccount(ID, APR);
        bank.depositInto(ID, 1000);
        bank.passTime(12);
        assertEquals(12, bank.getAccounts().get(ID).getMonths());
    }

    @Test
    void ensure_apr_is_being_calculated_and_added_to_balances_correctly_after_passing_time_by_a_month() {
        bank.openCheckingAccount(ID, APR_TWO);
        bank.depositInto(ID, 5000);
        bank.openSavingsAccount(ID_DIFFERENT, APR_TWO);
        bank.depositInto(ID_DIFFERENT, 80);
        bank.openCDAccount(ID_DIFFERENT + "1", APR, 1000);
        bank.withdrawFrom(ID_DIFFERENT + "1", 1000);
        bank.passTime(1);
        Account actualOne = bank.getAccounts().get(ID);
        Account actualTwo = bank.getAccounts().get(ID_DIFFERENT);
        Account actualThree = bank.getAccounts().get(ID_DIFFERENT + "1");
        assertEquals(5002.50, Math.round(actualOne.getBalance() * 100.0) / 100.0);
        assertEquals(80.04, Math.round(actualTwo.getBalance() * 100.0) / 100.0);
        assertNull(actualThree);
    }

    @Test
    void ensure_apr_is_being_calculated_and_added_to_balances_correctly_after_passing_time_by_two_months() {
        bank.openCheckingAccount(ID, APR_TWO);
        bank.depositInto(ID, 5000);
        bank.openSavingsAccount(ID_DIFFERENT, APR_TWO);
        bank.openCDAccount(ID_DIFFERENT + "1", APR_TWO + 1, 2000);
        bank.passTime(2);
        assertEquals(5005.00, Math.round(bank.getAccounts().get(ID).getBalance() * 100.0) / 100.0);
        assertNull(bank.getAccounts().get(ID_DIFFERENT));
        assertEquals(2021.43, Math.round(bank.getAccounts().get(ID_DIFFERENT + "1").getBalance() * 100.0) / 100.0);
    }

    @Test
    void pass_time_by_two_months_with_hundred_dollar_balance() {
        bank.openSavingsAccount(ID, APR_TWO);
        bank.depositInto(ID, 100);
        bank.passTime(1);
        assertEquals(100.05, Math.round(bank.getAccounts().get(ID).getBalance() * 100.0) / 100.0);
    }


}
