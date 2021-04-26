package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static banking.BankTest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CDAccountTest {

    private CDAccount account;

    @BeforeEach
    void setup() {
        account = new CDAccount(ID, APR, AMOUNT);
    }

    @Test
    void cd_balance_is_amount() {
        assertEquals(AMOUNT, account.getBalance());
    }

    @Test
    void cd_is_not_transferable() {
        assertFalse(account.canTransfer());
    }
}
