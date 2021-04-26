package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static banking.BankTest.AMOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckingAccountTest {

    private CheckingAccount account;

    @BeforeEach
    void setup() {
        account = new CheckingAccount("12345678", 0.01);
    }

    @Test
    void initial_balance_of_account_must_be_zero() {
        assertEquals(0, account.getBalance());
    }

    @Test
    void deposit_into_checking_account() {
        account.deposit(AMOUNT);
        assertEquals(AMOUNT, account.getBalance());
    }

    @Test
    void deposit_twice_into_checking_account() {
        account.deposit(AMOUNT);
        account.deposit(AMOUNT);
        assertEquals(AMOUNT * 2, account.getBalance());
    }

    @Test
    void withdraw_from_checking_account() {
        account.deposit(AMOUNT * 2);
        account.withdraw(AMOUNT);
        assertEquals(AMOUNT, account.getBalance());
    }

    @Test
    void withdraw_twice_from_checking_account() {
        account.deposit(AMOUNT * 2);
        account.withdraw(AMOUNT);
        account.withdraw(AMOUNT);
        assertEquals((AMOUNT * 2) - (AMOUNT * 2), account.getBalance());
    }

    @Test
    void withdraw_nothing_from_a_empty_checking_account() {
        account.withdraw(0);
        assertEquals(0, account.getBalance());
    }

    @Test
    void withdraw_the_entire_balance_of_an_checking_account() {
        account.deposit(AMOUNT);
        account.withdraw(AMOUNT);
        assertEquals(0, account.getBalance());
    }

}
