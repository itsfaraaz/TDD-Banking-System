package banking;

public class CommandInput {
    public String[] commandToArray(String commandAsString) {
        String[] commandAsArray = commandAsString.stripTrailing().split(" ");
        return commandAsArray;
    }
}
