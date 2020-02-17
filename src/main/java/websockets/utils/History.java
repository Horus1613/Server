package websockets.utils;

import java.util.ArrayList;

public class History {
    private ArrayList<String> history = new ArrayList<>();

    public void add(String data) {
        history.add(data);
    }

    public String returnAll() {
        StringBuilder result = new StringBuilder();
        history.forEach(i -> {
            result.append(i);
            result.append('\n');
        });
        return result.toString();
    }

    public void clear() {
        history.clear();
    }
}
