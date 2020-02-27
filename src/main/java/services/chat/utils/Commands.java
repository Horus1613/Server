package services.chat.utils;

public enum Commands {
    BAN,
    UNBAN,
    CLEAR_GLOBAL,
    NOT_FOUND;

    public static Commands getByIndex(int index){
        switch (index){
            case 0:
                return Commands.BAN;
            case 1:
                return Commands.UNBAN;
            case 2:
                return Commands.CLEAR_GLOBAL;
        }
        return Commands.NOT_FOUND;
    }
}
