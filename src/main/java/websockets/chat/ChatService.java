package websockets.chat;

import models.User;
import org.eclipse.jetty.websocket.api.Session;
import websockets.utils.History;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatService {
    Set<ChatWebSocket> webSockets;
    Set<User> usersOnline;
    History history = new History();

    public ChatService() {
        this.webSockets = Collections.newSetFromMap(new ConcurrentHashMap<>());
        this.usersOnline = Collections.newSetFromMap(new ConcurrentHashMap<>());
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

    public void updateOnline() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ChatWebSocket socket : webSockets) {
            usersOnline.clear();
            usersOnline.add(socket.user);
        }
        usersOnline.forEach(i -> stringBuilder.append(i.getLogin()).append('\n'));
        for (ChatWebSocket socket : webSockets) {
            try {
                socket.sendString("!u" + stringBuilder.toString());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void messageConnectivity(String login, boolean isJoin) {
        if (isJoin) {
            sendMessage(currentTime() + login + " joined!", false);
        } else {
            sendMessage(currentTime() + login + " left!", false);
        }
    }

    public void messageBanned(String login, boolean isBanned) {
        if (isBanned) {
            sendMessage(currentTime() + "User " + login + " was banned!", false);
        } else {
            sendMessage(currentTime() + "User " + login + " was unbanned!", false);
        }
    }

    public void regularMessage(String login, String data) {
        sendMessage(currentTime() + login + ": " + data, false);
    }

    private String currentTime() {
        String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        return time + "\t\t";
    }

    public void printHistory(Session session) {
        try {
            session.getRemote().sendString(history.returnAll());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean loginError(String login) {
        return login==null;
    }

    public void add(ChatWebSocket webSocket) {
        webSockets.add(webSocket);
    }

    public void remove(ChatWebSocket webSocket) {
        webSockets.remove(webSocket);
    }
}

