package banking;

import org.junit.jupiter.api.Test;

import static banking.AccountTest.BALANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BankTest {

    public static String ID = "12345678";
    public static double APR = 0.01;

    @Test
    void bank_initially_has_no_accounts() {
        Bank bank = new Bank();
        assertTrue(bank.getAccounts().isEmpty());
    }

    @Test
    void open_a_checking_account() {
        Bank bank = new Bank();
        bank.openCheckingAccount(ID, APR);
        assertEquals(0, bank.getAccounts().get(ID).getBalance());
    }

    @Test
    void open_a_savings_account() {
        Bank bank = new Bank();
        bank.openSavingsAccount(ID, APR);
        assertEquals(0, bank.getAccounts().get(ID).getBalance());
    }

    @Test
    void open_a_cd_account() {
        Bank bank = new Bank();
        bank.openCDAccount(ID, APR, BALANCE);
        assertEquals(1000, bank.getAccounts().get(ID).getBalance());
    }


}
