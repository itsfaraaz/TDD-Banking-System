package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MasterControlTest {
    private MasterControl masterControl;
    private List<String> input;
    
    private final String SAMPLE_CREATE_CHECKING_ACCOUNT_COMMAND = "create checking 12345678 0.6";
    private final String SAMPLE_CREATE_CHECKING_ACCOUNT_OUTPUT = "Checking 12345678 500.00 0.60";
    private final String SAMPLE_DEPOSIT_COMMAND_INTO_12345678 = "deposit 12345678 1000";
    private final String SAMPLE_CREATE_SAVINGS_COMMAND = "create savings 12345678 3";
    private final String DEPOSIT_100_INTO_ACCOUNT_12345678 = "deposit 12345678 100";
    private final String SAMPLE_CREATE_CD_ACCOUNT_COMMAND = "create cd 12345678 0.01 5000";
    private final String SAMPLE_CREATE_CD_ACCOUNT_OUTPUT = "Cd 12345678 5000.00 0.01";
    private final String SAMPLE_CREATE_SAVINGS_OUTPUT = "Savings 12345678 0.00 3.00";

    @BeforeEach
    void setup() {
        input = new ArrayList<>();
        Bank bank = new Bank();
        masterControl = new MasterControl(new CommandValidator(bank), new CommandProcessor(bank), new CommandStorage(bank), new CommandInput());
    }

    private void assertSingleCommand(String command, List<String> actual) {
        assertEquals(1, actual.size());
        assertEquals(command, actual.get(0));
    }

    @Test
    void unexpected_extra_spaces_in_beginning_is_invalid() {
        input.add(" create savings 12345678 0.01");
        assertSingleCommand(" create savings 12345678 0.01", masterControl.start(input));
    }

    @Test
    void unexpected_extra_spaces_in_middle_is_invalid() {
        input.add("create  checking 12345678 0.01");
        assertSingleCommand("create  checking 12345678 0.01", masterControl.start(input));
    }

    @Test
    void unexpected_extra_spaces_at_the_end_is_invalid() {
        input.add("create checking 12345678 0.01  ");
        assertSingleCommand("Checking 12345678 0.00 0.01", masterControl.start(input));
    }

    @Test
    void invalid_create_command_with_typo() {
        input.add("cret checking 12345678 0.01");
        assertSingleCommand("cret checking 12345678 0.01", masterControl.start(input));
    }

    @Test
    void invalid_deposit_command_with_typo() {
        input.add("depositt 12345678 1000");
        assertSingleCommand("depositt 12345678 1000", masterControl.start(input));
    }

    @Test
    void invalid_withdraw_command_with_typo() {
        input.add("withdrew 12345678 1000");
        assertSingleCommand("withdrew 12345678 1000", masterControl.start(input));
    }

    @Test
    void invalid_transfer_command_with_typo() {
        input.add("tranfar 12345678 12345677 1000");
        assertSingleCommand("tranfar 12345678 12345677 1000", masterControl.start(input));
    }

    @Test
    void invalid_pass_command_with_typo() {
        input.add("pas 12");
        assertSingleCommand("pas 12", masterControl.start(input));
    }

    @Test
    void two_invalid_commands_with_typos() {
        input.add("creat checking 12345678 0.01");
        input.add("withdrew 12345678 1000");
        List<String> result = masterControl.start(input);
        assertEquals(2, result.size());
        assertEquals("creat checking 12345678 0.01", result.get(0));
        assertEquals("withdrew 12345678 1000", result.get(1));
    }

    @Test
    void cannot_create_an_account_with_the_same_ID() {
        input.add("create checking 12345678 0.01");
        input.add("create checking 12345678 0.01");
        List<String> result = masterControl.start(input);
        assertEquals(2, result.size());
        assertEquals("Checking 12345678 0.00 0.01", result.get(0));
        assertEquals("create checking 12345678 0.01", result.get(1));
    }


    @Test
    void cannot_create_checking_account_with_typo() {
        input.add("create chcking 12345678 0.01");
        assertSingleCommand("create chcking 12345678 0.01", masterControl.start(input));
    }

    @Test
    void cannot_create_savings_account_with_typo() {
        input.add("create saving 12345678 0.01");
        assertSingleCommand("create saving 12345678 0.01", masterControl.start(input));
    }

    @Test
    void cannot_create_cd_account_without_starting_balance() {
        input.add("create cd 12345678 0.01");
        assertSingleCommand("create cd 12345678 0.01", masterControl.start(input));
    }

    @Test
    void cannot_deposit_into_account_that_does_not_exist() {
        input.add("create checking 12345676 0.01");
        input.add(DEPOSIT_100_INTO_ACCOUNT_12345678);

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Checking 12345676 0.00 0.01", actual.get(0));
        assertEquals(DEPOSIT_100_INTO_ACCOUNT_12345678, actual.get(1));
    }

    @Test
    void cannot_deposit_into_a_CD_account() {
        input.add(SAMPLE_CREATE_CD_ACCOUNT_COMMAND);
        input.add(DEPOSIT_100_INTO_ACCOUNT_12345678);

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals(SAMPLE_CREATE_CD_ACCOUNT_OUTPUT, actual.get(0));
        assertEquals(DEPOSIT_100_INTO_ACCOUNT_12345678, actual.get(1));
    }

    @Test
    void cannot_depost_more_than_a_thousand_into_checking_accounts() {
        input.add(SAMPLE_CREATE_CHECKING_ACCOUNT_COMMAND);
        input.add(SAMPLE_DEPOSIT_COMMAND_INTO_12345678 + "1");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Checking 12345678 0.00 0.60", actual.get(0));
        assertEquals(SAMPLE_DEPOSIT_COMMAND_INTO_12345678 + "1", actual.get(1));
    }

    @Test
    void cannot_deposit_more_than_twenty_five_hundred_dollars_into_savings() {
        input.add(SAMPLE_CREATE_SAVINGS_COMMAND);
        input.add(SAMPLE_DEPOSIT_COMMAND_INTO_12345678 + "10");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals(SAMPLE_CREATE_SAVINGS_OUTPUT, actual.get(0));
        assertEquals(SAMPLE_DEPOSIT_COMMAND_INTO_12345678 + "10", actual.get(1));
    }

    @Test
    void can_make_deposits_into_checking_and_savings() {
        input.add("create checking 12345678 0.01");
        input.add("deposit 12345678 500");
        input.add("create savings 87654321 0.01");
        input.add("deposit 87654321 1250");

        List<String> actual = masterControl.start(input);

        assertEquals(4, actual.size());
        assertEquals("Checking 12345678 500.00 0.01", actual.get(0));
        assertEquals("deposit 12345678 500", actual.get(1));
        assertEquals("Savings 87654321 1250.00 0.01", actual.get(2));
        assertEquals("deposit 87654321 1250", actual.get(3));
    }

    @Test
    void cannot_create_an_account_that_is_not_an_eight_digit_number() {
        input.add("create checking 123ac738 0.01");
        assertSingleCommand("create checking 123ac738 0.01", masterControl.start(input));
    }

    @Test
    void cannot_withdraw_from_an_account_that_does_not_exist() {
        input.add(SAMPLE_CREATE_CHECKING_ACCOUNT_COMMAND);
        input.add("withdraw 23456789 200");
        List<String> actual = masterControl.start(input);
        assertEquals(2, actual.size());
        assertEquals("Checking 12345678 0.00 0.60", actual.get(0));
        assertEquals("withdraw 23456789 200", actual.get(1));
    }

    @Test
    void cannot_withdraw_more_than_four_hundred_from_checking() {
        input.add(SAMPLE_CREATE_CHECKING_ACCOUNT_COMMAND);
        input.add("deposit 12345678 500");
        input.add("withdraw 12345678 450");
        List<String> actual = masterControl.start(input);
        assertEquals(3, actual.size());
        assertEquals(SAMPLE_CREATE_CHECKING_ACCOUNT_OUTPUT, actual.get(0));
        assertEquals("deposit 12345678 500", actual.get(1));
        assertEquals("withdraw 12345678 450", actual.get(2));
    }

    @Test
    void cannot_withdraw_more_than_thousand_from_checking() {
        input.add(SAMPLE_CREATE_SAVINGS_COMMAND);
        input.add("deposit 12345678 2000");
        input.add("withdraw 12345678 1500");
        List<String> actual = masterControl.start(input);
        assertEquals(3, actual.size());
        assertEquals("Savings 12345678 2000.00 3.00", actual.get(0));
        assertEquals("deposit 12345678 2000", actual.get(1));
        assertEquals("withdraw 12345678 1500", actual.get(2));
    }

    @Test
    void cannot_withdraw_from_savings_account_twice_in_one_month() {
        input.add(SAMPLE_CREATE_SAVINGS_COMMAND);
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
    void cannot_withdraw_from_cd_younger_than_twelve_months() {
        input.add(SAMPLE_CREATE_CD_ACCOUNT_COMMAND);
        input.add("withdraw 12345678 5000");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals(SAMPLE_CREATE_CD_ACCOUNT_OUTPUT, actual.get(0));
        assertEquals("withdraw 12345678 5000", actual.get(1));
    }

    @Test
    void can_make_withdrawal_from_checking() {
        input.add(SAMPLE_CREATE_CHECKING_ACCOUNT_COMMAND);
        input.add(SAMPLE_DEPOSIT_COMMAND_INTO_12345678);
        input.add("withdraw 12345678 400");

        List<String> actual = masterControl.start(input);

        assertEquals(3, actual.size());
        assertEquals("Checking 12345678 600.00 0.60", actual.get(0));
        assertEquals(SAMPLE_DEPOSIT_COMMAND_INTO_12345678, actual.get(1));
        assertEquals("withdraw 12345678 400", actual.get(2));
    }

    @Test
    void can_make_withdrawal_from_savings() {
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
    void cannot_make_transfer_between_accounts_if_one_doesnt_exist() {
        input.add(SAMPLE_CREATE_CHECKING_ACCOUNT_COMMAND);
        input.add(DEPOSIT_100_INTO_ACCOUNT_12345678);
        input.add("transfer 12345678 87654321 100");

        List<String> actual = masterControl.start(input);

        assertEquals(3, actual.size());
        assertEquals("Checking 12345678 100.00 0.60", actual.get(0));
        assertEquals(DEPOSIT_100_INTO_ACCOUNT_12345678, actual.get(1));
        assertEquals("transfer 12345678 87654321 100", actual.get(2));
    }

    @Test
    void cannot_transfer_greater_than_four_hundred_between_checking_accounts() {
        input.add(SAMPLE_CREATE_CHECKING_ACCOUNT_COMMAND);
        input.add(SAMPLE_DEPOSIT_COMMAND_INTO_12345678);
        input.add("create checking 87654321 1.2");
        input.add("transfer 12345678 87654321 1000");

        List<String> actual = masterControl.start(input);

        assertEquals(4, actual.size());
        assertEquals("Checking 12345678 1000.00 0.60", actual.get(0));
        assertEquals(SAMPLE_DEPOSIT_COMMAND_INTO_12345678, actual.get(1));
        assertEquals("Checking 87654321 0.00 1.20", actual.get(2));
        assertEquals("transfer 12345678 87654321 1000", actual.get(3));
    }

    @Test
    void cannot_transfer_greater_than_thousand_between_savings_accounts() {
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
    void cannot_make_transfer_greater_than_four_hundred_from_checking_to_savings() {
        input.add(SAMPLE_CREATE_CHECKING_ACCOUNT_COMMAND);
        input.add(SAMPLE_DEPOSIT_COMMAND_INTO_12345678);
        input.add(SAMPLE_DEPOSIT_COMMAND_INTO_12345678);
        input.add("deposit 12345678 500");
        input.add("create savings 87654321 1.2");
        input.add("transfer 12345678 87654321 401");

        List<String> actual = masterControl.start(input);

        assertEquals(6, actual.size());
        assertEquals("Checking 12345678 2500.00 0.60", actual.get(0));
        assertEquals(SAMPLE_DEPOSIT_COMMAND_INTO_12345678, actual.get(1));
        assertEquals(SAMPLE_DEPOSIT_COMMAND_INTO_12345678, actual.get(2));
        assertEquals("deposit 12345678 500", actual.get(3));
        assertEquals("Savings 87654321 0.00 1.20", actual.get(4));
        assertEquals("transfer 12345678 87654321 401", actual.get(5));
    }

    @Test
    void can_make_transfer_from_checking_to_savings() {
        input.add(SAMPLE_CREATE_CHECKING_ACCOUNT_COMMAND);
        input.add(SAMPLE_DEPOSIT_COMMAND_INTO_12345678);
        input.add("create savings 87654321 1.2");
        input.add("transfer 12345678 87654321 200");

        List<String> actual = masterControl.start(input);

        assertEquals(5, actual.size());
        assertEquals("Checking 12345678 800.00 0.60", actual.get(0));
        assertEquals(SAMPLE_DEPOSIT_COMMAND_INTO_12345678, actual.get(1));
        assertEquals("transfer 12345678 87654321 200", actual.get(2));
        assertEquals("Savings 87654321 200.00 1.20", actual.get(3));
        assertEquals("transfer 12345678 87654321 200", actual.get(4));
    }

    @Test
    void cannot_withdraw_and_transfer_within_the_same_month_from_savings() {
        input.add("create savings 12345678 0.6");
        input.add(SAMPLE_DEPOSIT_COMMAND_INTO_12345678);
        input.add("create checking 87654321 1.2");
        input.add("withdraw 12345678 200");
        input.add("transfer 12345678 87654321 300");

        List<String> actual = masterControl.start(input);

        System.out.println(actual);

        assertEquals(6, actual.size());
        assertEquals("Savings 12345678 500.00 0.60", actual.get(0));
        assertEquals(SAMPLE_DEPOSIT_COMMAND_INTO_12345678, actual.get(1));
        assertEquals("withdraw 12345678 200", actual.get(2));
        assertEquals("transfer 12345678 87654321 300", actual.get(3));
        assertEquals("Checking 87654321 300.00 1.20", actual.get(4));
        assertEquals("transfer 12345678 87654321 300", actual.get(5));
    }

    @Test
    void pass_time_with_all_three_different_accounts_present_in_bank() {
        input.add("create checking 12345678 1.50");
        input.add("deposit 12345678 80");
        input.add("create savings 87654321 1.50");
        input.add("deposit 87654321 2500");
        input.add("deposit 87654321 2500");
        input.add("create checking 23456789 0.6");
        input.add("create cd 98765432 1 5000");
        input.add("pass 1");

        List<String> actual = masterControl.start(input);

        assertEquals(6, actual.size());
        assertEquals("Checking 12345678 80.10 1.50", actual.get(0));
        assertEquals("deposit 12345678 80", actual.get(1));
        assertEquals("Savings 87654321 5006.25 1.50", actual.get(2));
        assertEquals("deposit 87654321 2500", actual.get(3));
        assertEquals("deposit 87654321 2500", actual.get(4));
        assertEquals("Cd 98765432 5016.68 1.00", actual.get(5));
    }

    @Test
    void able_to_create_another_account_with_a_previously_closed_account_id() {
        input.add(SAMPLE_CREATE_CHECKING_ACCOUNT_COMMAND);
        input.add(DEPOSIT_100_INTO_ACCOUNT_12345678);
        input.add("create savings 87654321 0.6");
        input.add("pass 1");
        input.add("create cd 87654321 0.6 10000");

        List<String> actual = masterControl.start(input);

        assertEquals(3, actual.size());
        assertEquals("Checking 12345678 100.05 0.60", actual.get(0));
        assertEquals(DEPOSIT_100_INTO_ACCOUNT_12345678, actual.get(1));
        assertEquals("Cd 87654321 10000.00 0.60", actual.get(2));
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
