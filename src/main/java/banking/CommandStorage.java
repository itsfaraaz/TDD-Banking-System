package banking;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class CommandStorage {

    private Bank bank;
    private List<String> invalidCommands = new ArrayList<>();
    private Map<String, List<String>> validCommands = new HashMap<>();

    public CommandStorage(Bank bank) {
        this.bank = bank;
    }

    public void storeValidCommand(String command) {
        CommandInput input = new CommandInput();
        String[] commandArguments = input.commandToArray(command);

        if (commandArguments[0].equalsIgnoreCase("create") ||
                commandArguments[0].equalsIgnoreCase("withdraw") ||
                commandArguments[0].equalsIgnoreCase("deposit")) {
            insertIntoMap(validCommands, commandArguments[1], command);
        } else if (commandArguments[0].equalsIgnoreCase("transfer")) {
            insertIntoMap(validCommands, commandArguments[1], command);
            insertIntoMap(validCommands, commandArguments[2], command);
        }
    }

    private void insertIntoMap(Map<String, List<String>> map, String accountID, String command) {
        if (map.get(accountID) != null) {
            map.get(accountID).add(command);
        } else if (map.get(accountID) == null) {
            map.put(accountID, new ArrayList<>());
            map.get(accountID).add(command);
        }
    }

    public void storeInvalidCommand(String command) {
        invalidCommands.add(command);
    }

    public List<String> getOutput() {
        List<String> output = new ArrayList<>();
        for (String accountID : bank.getAccountOrder()) {
            output.add(getFormattedAccountStatus(accountID));
            if (validCommands.get(accountID) != null) {
                output.addAll(validCommands.get(accountID));
            }
        }
        output.addAll(invalidCommands);
        return output;
    }

    private String getFormattedAccountStatus(String accountID) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        Account account = bank.getAccounts().get(accountID);
        String formattedBalance = decimalFormat.format(account.getBalance());
        String formattedAPR = decimalFormat.format(account.getAPR());
        return account.getAccountType() + " " + account.getID() + " " + formattedBalance + " " + formattedAPR;
    }

    public List<String> getInvalidCommands() {
        return invalidCommands;
    }

    public Map<String, List<String>> getValidCommands() {
        return validCommands;
    }

}
