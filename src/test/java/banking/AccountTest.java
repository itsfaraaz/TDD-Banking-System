package banking;

import org.junit.jupiter.api.Test;

import static banking.BankTest.APR;
import static banking.BankTest.ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountTest {

    static final int BALANCE = 1000;

    @Test

    void open_checking_account() {
        CheckingAccount account = new CheckingAccount(ID, APR);
        assertEquals(ID, account.getID());
        assertEquals(APR, account.getAPR());
    }

    @Test
    void create_savings_account() {
        SavingsAccount account = new SavingsAccount(ID, APR);
        assertEquals(ID, account.getID());
        assertEquals(APR, account.getAPR());
    }

    @Test
    void create_cd_account() {
        CDAccount account = new CDAccount(ID, APR, BALANCE);
        assertEquals(ID, account.getID());
        assertEquals(APR, account.getAPR());
        assertEquals(BALANCE, account.getBalance());
    }

}
