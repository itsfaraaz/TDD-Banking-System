package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MasterControlTest {
    MasterControl masterControl;
    private List<String> input;

    @BeforeEach
    public void setUp() {
        input = new ArrayList<>();
        Bank bank = new Bank();
        masterControl = new MasterControl(new CommandValidator(bank), new CommandProcessor(bank),
                new CommandStorage(bank), new CommandInput());
    }

    private void assertSingleCommand(String command, List<String> actual) {
        assertEquals(1, actual.size());
        assertEquals(command, actual.get(0));
    }

    @Test
    public void extra_spaces_in_the_beginning_of_command_is_invalid() {
        input.add(" create checking 12345678 1.0");

        List<String> actual = masterControl.start(input);

        assertSingleCommand(" create checking 12345678 1.0", actual);
    }

    @Test
    public void extra_spaces_in_the_middle_of_command_is_invalid() {
        input.add("create  checking 12345678 1.0");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("create  checking 12345678 1.0", actual);
    }

    @Test
    public void extra_spaces_at_end_of_command_is_valid() {
        input.add("create checking 12345678 1.0  ");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("Checking 12345678 0.00 1.00", actual);
    }

    @Test
    public void typo_in_create_command_is_invalid() {
        input.add("creat checking 12345678 1.0");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("creat checking 12345678 1.0", actual);
    }

    @Test
    public void typo_in_deposit_command_is_invalid() {
        input.add("depositt 12345678 100");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("depositt 12345678 100", actual);
    }

    @Test
    public void typo_in_withdraw_command_is_invalid() {
        input.add("withdrew 12345678 100");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("withdrew 12345678 100", actual);
    }

    @Test
    public void typo_in_transfer_command_is_invalid() {
        input.add("tranfar 12345678 987654321 100");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("tranfar 12345678 987654321 100", actual);
    }

    @Test
    public void typo_in_pass_time_command_is_invalid() {
        input.add("pas 1");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("pas 1", actual);
    }

    @Test
    public void two_typo_commands_both_invalid() {
        input.add("creat checking 12345678 1.0");
        input.add("depositt 12345678 100");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("creat checking 12345678 1.0", actual.get(0));
        assertEquals("depositt 12345678 100", actual.get(1));
    }

    @Test
    public void invalid_to_create_accounts_with_same_ID() {
        input.add("create checking 12345678 1.0");
        input.add("create checking 12345678 1.0");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
        assertEquals("create checking 12345678 1.0", actual.get(1));
    }


    @Test
    public void typo_in_checking_account_type_is_invalid() {
        input.add("create ch3cking 12345678 1.0");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("create ch3cking 12345678 1.0", actual);
    }

    @Test
    public void typo_in_savings_account_type_is_invalid() {
        input.add("create s4vings 12345678 1.0");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("create s4vings 12345678 1.0", actual);
    }

    @Test
    public void missing_amount_when_creating_cd_is_invalid() {
        input.add("create cd 12345678 1.0");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("create cd 12345678 1.0", actual);
    }

    @Test
    public void invalid_to_deposit_into_account_that_doesnt_exists() {
        input.add("create checking 23456789 0.345");
        input.add("deposit 12345678 100");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Checking 23456789 0.00 0.34", actual.get(0));
        assertEquals("deposit 12345678 100", actual.get(1));
    }

    @Test
    public void invalid_to_deposit_into_CD() {
        input.add("create cd 12345678 1.0 5000");
        input.add("deposit 12345678 100");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Cd 12345678 5000.00 1.00", actual.get(0));
        assertEquals("deposit 12345678 100", actual.get(1));
    }

    @Test
    public void invalid_to_deposit_more_than_1000_into_checking() {
        input.add("create checking 12345678 1.0");
        input.add("deposit 12345678 2000");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
        assertEquals("deposit 12345678 2000", actual.get(1));
    }

    @Test
    public void invalid_to_deposit_more_than_2500_into_savings() {
        input.add("create savings 12345678 1.0");
        input.add("deposit 12345678 3000");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Savings 12345678 0.00 1.00", actual.get(0));
        assertEquals("deposit 12345678 3000", actual.get(1));
    }

    @Test
    public void valid_deposit_into_checking_and_savings() {
        input.add("create checking 12345678 1.0");
        input.add("deposit 12345678 500");
        input.add("create savings 87654321 1.0");
        input.add("deposit 87654321 1250");

        List<String> actual = masterControl.start(input);

        assertEquals(4, actual.size());
        assertEquals("Checking 12345678 500.00 1.00", actual.get(0));
        assertEquals("deposit 12345678 500", actual.get(1));
        assertEquals("Savings 87654321 1250.00 1.00", actual.get(2));
        assertEquals("deposit 87654321 1250", actual.get(3));
    }

    @Test
    public void id_is_not_a_8_digit_number_when_creating_account_is_invalid() {
        input.add("create checking 123abc78 1.0");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("create checking 123abc78 1.0", actual);
    }

    @Test
    public void invalid_to_withdraw_from_an_account_that_doesnt_exist() {
        input.add("create checking 12345678 0.6");
        input.add("withdraw 23456789 200");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Checking 12345678 0.00 0.60", actual.get(0));
        assertEquals("withdraw 23456789 200", actual.get(1));
    }

    @Test
    public void invalid_to_withdraw_more_than_400_from_checking() {
        input.add("create checking 12345678 0.6");
        input.add("deposit 12345678 500");
        input.add("withdraw 12345678 450");

        List<String> actual = masterControl.start(input);

        assertEquals(3, actual.size());
        assertEquals("Checking 12345678 500.00 0.60", actual.get(0));
        assertEquals("deposit 12345678 500", actual.get(1));
        assertEquals("withdraw 12345678 450", actual.get(2));
    }

    @Test
    public void invalid_to_withdraw_more_than_1000_from_savings() {
        input.add("create savings 12345678 3");
        input.add("deposit 12345678 2000");
        input.add("withdraw 12345678 1500");

        List<String> actual = masterControl.start(input);

        assertEquals(3, actual.size());
        assertEquals("Savings 12345678 2000.00 3.00", actual.get(0));
        assertEquals("deposit 12345678 2000", actual.get(1));
        assertEquals("withdraw 12345678 1500", actual.get(2));
    }

    @Test
    public void invalid_to_withdraw_twice_in_one_month_from_savings() {
        input.add("create savings 12345678 3");
        input.add("deposit 12345678 2000");
        input.add("withdraw 12345678 200");
        input.add("withdraw 12345678 300");

        List<String> actual = masterControl.start(input);

        assertEquals(4, actual.size());
        assertEquals("Savings 12345678 1500.00 3.00", actual.get(0));
        assertEquals("deposit 12345678 2000", actual.get(1));
        assertEquals("withdraw 12345678 200", actual.get(2));
        assertEquals("withdraw 12345678 300", actual.get(3));
    }

    @Test
    public void invalid_to_withdraw_from_cd_if_not_12_months_old() {
        input.add("create cd 12345678 0.6 5000");
        input.add("withdraw 12345678 5000");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Cd 12345678 5000.00 0.60", actual.get(0));
        assertEquals("withdraw 12345678 5000", actual.get(1));
    }

    @Test
    public void valid_withdraw_from_checking() {
        input.add("create checking 12345678 0.6");
        input.add("deposit 12345678 1000");
        input.add("withdraw 12345678 400");

        List<String> actual = masterControl.start(input);

        assertEquals(3, actual.size());
        assertEquals("Checking 12345678 600.00 0.60", actual.get(0));
        assertEquals("deposit 12345678 1000", actual.get(1));
        assertEquals("withdraw 12345678 400", actual.get(2));
    }

    @Test
    public void valid_withdraw_from_savings() {
        input.add("create savings 12345678 0.6");
        input.add("depOsit 12345678 2500");
        input.add("withdraw 12345678 1000");

        List<String> actual = masterControl.start(input);

        assertEquals(3, actual.size());
        assertEquals("Savings 12345678 1500.00 0.60", actual.get(0));
        assertEquals("depOsit 12345678 2500", actual.get(1));
        assertEquals("withdraw 12345678 1000", actual.get(2));
    }

    @Test
    public void invalid_to_transfer_between_accounts_when_either_account_does_not_exist() {
        input.add("create checking 12345678 0.6");
        input.add("deposit 12345678 100");
        input.add("transfer 12345678 87654321 100");

        List<String> actual = masterControl.start(input);

        assertEquals(3, actual.size());
        assertEquals("Checking 12345678 100.00 0.60", actual.get(0));
        assertEquals("deposit 12345678 100", actual.get(1));
        assertEquals("transfer 12345678 87654321 100", actual.get(2));
    }

    @Test
    public void invalid_to_transfer_1000_between_checking_accounts() {
        input.add("create checking 12345678 0.6");
        input.add("deposit 12345678 1000");
        input.add("create checking 87654321 1.2");
        input.add("transfer 12345678 87654321 1000");

        List<String> actual = masterControl.start(input);

        assertEquals(4, actual.size());
        assertEquals("Checking 12345678 1000.00 0.60", actual.get(0));
        assertEquals("deposit 12345678 1000", actual.get(1));
        assertEquals("Checking 87654321 0.00 1.20", actual.get(2));
        assertEquals("transfer 12345678 87654321 1000", actual.get(3));
    }

    @Test
    public void invalid_to_transfer_2500_between_savings_accounts() {
        input.add("create savings 12345678 0.6");
        input.add("deposit 12345678 2500");
        input.add("create savings 87654321 1.2");
        input.add("transfer 12345678 87654321 2500");

        List<String> actual = masterControl.start(input);

        assertEquals(4, actual.size());
        assertEquals("Savings 12345678 2500.00 0.60", actual.get(0));
        assertEquals("deposit 12345678 2500", actual.get(1));
        assertEquals("Savings 87654321 0.00 1.20", actual.get(2));
        assertEquals("transfer 12345678 87654321 2500", actual.get(3));
    }

    @Test
    public void invalid_to_transfer_2500_from_checking_to_savings() {
        input.add("create checking 12345678 0.6");
        input.add("deposit 12345678 1000");
        input.add("deposit 12345678 1000");
        input.add("deposit 12345678 500");
        input.add("create savings 87654321 1.2");
        input.add("transfer 12345678 87654321 2500");

        List<String> actual = masterControl.start(input);

        assertEquals(6, actual.size());
        assertEquals("Checking 12345678 2500.00 0.60", actual.get(0));
        assertEquals("deposit 12345678 1000", actual.get(1));
        assertEquals("deposit 12345678 1000", actual.get(2));
        assertEquals("deposit 12345678 500", actual.get(3));
        assertEquals("Savings 87654321 0.00 1.20", actual.get(4));
        assertEquals("transfer 12345678 87654321 2500", actual.get(5));
    }

    @Test
    public void valid_transfer_from_checking_to_savings() {
        input.add("create checking 12345678 0.6");
        input.add("deposit 12345678 1000");
        input.add("create savings 87654321 1.2");
        input.add("transfer 12345678 87654321 200");

        List<String> actual = masterControl.start(input);

        assertEquals(5, actual.size());
        assertEquals("Checking 12345678 800.00 0.60", actual.get(0));
        assertEquals("deposit 12345678 1000", actual.get(1));
        assertEquals("transfer 12345678 87654321 200", actual.get(2));
        assertEquals("Savings 87654321 200.00 1.20", actual.get(3));
        assertEquals("transfer 12345678 87654321 200", actual.get(4));
    }

    @Test
    public void invalid_to_withdraw_and_transfer_in_one_month_with_savings() {
        input.add("create savings 12345678 0.6");
        input.add("deposit 12345678 1000");
        input.add("create checking 87654321 1.2");
        input.add("withdraw 12345678 200");
        input.add("transfer 12345678 87654321 300");

        List<String> actual = masterControl.start(input);

        System.out.println(actual);

        assertEquals(6, actual.size());
        assertEquals("Savings 12345678 500.00 0.60", actual.get(0));
        assertEquals("deposit 12345678 1000", actual.get(1));
        assertEquals("withdraw 12345678 200", actual.get(2));
        assertEquals("transfer 12345678 87654321 300", actual.get(3));
        assertEquals("Checking 87654321 300.00 1.20", actual.get(4));
        assertEquals("transfer 12345678 87654321 300", actual.get(5));
    }

    @Test
    public void pass_time_one_month_with_accounts_that_have_balances_zero_over_100_inbetween_0_and_100_and_a_cd_present() {
        input.add("create checking 12345678 1");
        input.add("deposit 12345678 80");
        input.add("create savings 87654321 1");
        input.add("deposit 87654321 2500");
        input.add("deposit 87654321 2500");
        input.add("create checking 23456789 0.6");
        input.add("create cd 98765432 1 5000");
        input.add("pass 1");

        List<String> actual = masterControl.start(input);

        assertEquals(6, actual.size());
        assertEquals("Checking 12345678 80.06 1.00", actual.get(0));
        assertEquals("deposit 12345678 80", actual.get(1));
        assertEquals("Savings 87654321 5004.16 1.00", actual.get(2));
        assertEquals("deposit 87654321 2500", actual.get(3));
        assertEquals("deposit 87654321 2500", actual.get(4));
        assertEquals("Cd 98765432 5016.68 1.00", actual.get(5));
    }

    @Test
    public void valid_to_create_new_account_with_a_closed_accounts_id() {
        input.add("create checking 12345678 1");
        input.add("deposit 12345678 55");
        input.add("create savings 87654321 1");
        input.add("pass 1");
        input.add("create cd 87654321 1 5000");

        List<String> actual = masterControl.start(input);

        assertEquals(3, actual.size());
        assertEquals("Checking 12345678 55.04 1.00", actual.get(0));
        assertEquals("deposit 12345678 55", actual.get(1));
        assertEquals("Cd 87654321 5000.00 1.00", actual.get(2));
    }

    @Test
    void sample_make_sure_this_passes_unchanged_or_you_will_fail() {
        input.add("Create savings 12345678 0.6");
        input.add("Deposit 12345678 700");
        input.add("Deposit 12345678 5000");
        input.add("creAte cHecKing 98765432 0.01");
        input.add("Deposit 98765432 300");
        input.add("Transfer 98765432 12345678 300");
        input.add("Pass 1");
        input.add("Create cd 23456789 1.2 2000");
        List<String> actual = masterControl.start(input);

        assertEquals(5, actual.size());
        assertEquals("Savings 12345678 1000.50 0.60", actual.get(0));
        assertEquals("Deposit 12345678 700", actual.get(1));
        assertEquals("Transfer 98765432 12345678 300", actual.get(2));
        assertEquals("Cd 23456789 2000.00 1.20", actual.get(3));
        assertEquals("Deposit 12345678 5000", actual.get(4));
    }
}
