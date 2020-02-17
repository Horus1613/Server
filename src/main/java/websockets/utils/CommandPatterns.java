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


    public int patternValidate(String data) {
        for (int i = 0; i < patterns.size(); i++) {
            if (patterns.get(i).matcher(data).matches()) {
                return i;
            }
        }
        return -1;
    }

    public String userName(String data, int patternIndex){
        Matcher matcher = patterns.get(patternIndex).matcher(data);
        matcher.find();
        return matcher.group(1);
    }
}
