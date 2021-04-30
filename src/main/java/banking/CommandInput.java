package banking;

class CommandInput {
    public String[] commandToArray(String commandAsString) {
        return commandAsString.stripTrailing().split(" ");
    }
}
