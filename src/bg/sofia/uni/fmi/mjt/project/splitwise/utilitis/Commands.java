package bg.sofia.uni.fmi.mjt.project.splitwise.utilitis;

public enum Commands {

    ADD("add"),
    CREATE("create"),
    GET_STATUS("get-status"),
    HISTORY_OF_PAYMENT("history-of-payment"),
    LOGIN("login"),
    PAYED("payed"),
    PAYED_GROUP("payed-group"),
    SIGN_UP("sgin-up"),
    SPLIT("split"),
    SPLIT_GROUP("split-group");


    private final String command;

    Commands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }
}
