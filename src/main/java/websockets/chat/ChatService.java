package websockets.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatService {
    Set<ChatWebSocket> webSockets;
    ArrayList<String> history = new ArrayList<>();

    public ChatService() {
        this.webSockets = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public void sendMessage(String data, boolean devMode) {
        if (!devMode) {
            history.add(data);
        }

        for (ChatWebSocket socket : webSockets) {
            try {
                socket.sendString(data);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public void updateOnline(){
        StringBuilder onlineUsers = new StringBuilder();
        for(ChatWebSocket socket : webSockets){
            onlineUsers.append(socket.login).append("\n");
        }
        for(ChatWebSocket socket : webSockets){
            try{
                socket.sendString("!u"+onlineUsers.toString());
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }


    public void add(ChatWebSocket webSocket) {
        webSockets.add(webSocket);
    }

    public void remove(ChatWebSocket webSocket) {
        webSockets.remove(webSocket);
    }
}

