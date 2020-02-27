package services.chat.utils;

import dao.UserDAO;
import services.chat.ChatService;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandPatterns {
    private ArrayList<Pattern> patterns;
    private static final String LOCAL_CLEAR_COMMAND = "/clear";

    public CommandPatterns() {
        patterns = new ArrayList<>();
        patterns.add(Pattern.compile("^/ban ([\\S]+).*"));
        patterns.add(Pattern.compile("^/unban ([\\S]+).*"));
        patterns.add(Pattern.compile("/clear -global"));
    }


    public Commands findPattern(String data) {
        for (int i = 0; i < patterns.size(); i++) {
            if (patterns.get(i).matcher(data).matches()) {
                return Commands.getByIndex(i);
            }
        }
        return Commands.NOT_FOUND;
    }

    public boolean validatePattern(String data){
        for (int i = 0; i < patterns.size(); i++) {
            if (patterns.get(i).matcher(data).matches()) {
                return true;
            }
        }
        return false;
    }

    private String userName(String data, Commands command){
        Matcher matcher = patterns.get(command.ordinal()).matcher(data);
        matcher.find();
        return matcher.group(1);
    }

    public void sendCommand(UserDAO userDAO, ChatService chatService, String data){
        Commands command = this.findPattern(data);
        String commandUsername;
        switch (command) {
            case BAN:
                commandUsername = this.userName(data, Commands.BAN);
                userDAO.banControlByLogin(commandUsername, true);
                chatService.messageBanned(commandUsername, true);
                break;
            case UNBAN:
                commandUsername = this.userName(data, Commands.UNBAN);
                userDAO.banControlByLogin(commandUsername, false);
                chatService.messageBanned(commandUsername, false);
                break;
            case CLEAR_GLOBAL:
                chatService.sendMessage(LOCAL_CLEAR_COMMAND);
                chatService.clearHistory();
                break;
        }
    }
}
