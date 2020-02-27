package services.chat.utils;

public enum Notify {
    JOIN("%sUser %s joined!"),
    LEFT("%sUser %s left!"),
    BAN("%sUser %s banned!"),
    UNBAN("%sUser %s unbanned!"),
    REGULAR("%s%s:%s");


    private final String value;

    Notify(String value) {
        this.value = value;
    }

    public String execute(String currentTime, String login) {
        return String.format(value, currentTime, login);
    }

    public String execute(String currentTime, String login, String data) {
        return String.format(value, currentTime, login, data);
    }
}
