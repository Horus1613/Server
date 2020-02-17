package websockets.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandPatterns {
    private ArrayList<Pattern> patterns;

    public CommandPatterns() {
        patterns = new ArrayList<>();
        patterns.add(Pattern.compile("^/ban ([\\S]+).*"));
        patterns.add(Pattern.compile("^/unban ([\\S]+).*"));
        patterns.add(Pattern.compile("/clear -global"));
    }


    public Commands patternValidate(String data) {
        for (int i = 0; i < patterns.size(); i++) {
            if (patterns.get(i).matcher(data).matches()) {
                return Commands.getByIndex(i);
            }
        }
        return Commands.NOT_FOUND;
    }

    public String userName(String data, Commands command){
        Matcher matcher = patterns.get(command.ordinal()).matcher(data);
        matcher.find();
        return matcher.group(1);
    }
}
