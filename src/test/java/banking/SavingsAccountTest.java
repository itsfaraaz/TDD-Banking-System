package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static banking.BankTest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SavingsAccountTest {
    private SavingsAccount account;

    @BeforeEach
    void setUp() {
        account = new SavingsAccount(ID, APR);
    }

    @Test
    void initial_balance_of_account_must_be_zero() {
        assertEquals(0, account.getBalance());
    }

    @Test
    void deposit_into_savings_account() {
        account.deposit(AMOUNT);
        assertEquals(AMOUNT, account.getBalance());
    }

    @Test
    void deposit_twice_into_savings_account() {
        account.deposit(AMOUNT);
        account.deposit(AMOUNT);
        assertEquals(AMOUNT * 2, account.getBalance());
    }

    @Test
    void withdraw_from_savings_account() {
        account.deposit(AMOUNT * 2);
        account.withdraw(AMOUNT);
        assertEquals(AMOUNT, account.getBalance());
    }

    @Test
    void withdraw_twice_from_savings_account() {
        account.deposit(AMOUNT * 2);
        account.withdraw(AMOUNT);
        account.withdraw(AMOUNT);
        assertEquals((AMOUNT * 2) - (AMOUNT * 2), account.getBalance());
    }

    @Test
    void withdraw_nothing_from_a_empty_savings_account() {
        account.withdraw(0);
        assertEquals(0, account.getBalance());
    }

    @Test
    void withdraw_the_entire_balance_of_an_savings_account() {
        account.deposit(AMOUNT);
        account.withdraw(AMOUNT);
        assertEquals(0, account.getBalance());
    }
}
