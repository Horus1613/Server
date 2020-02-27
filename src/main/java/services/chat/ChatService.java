package services.chat;

import models.ChatWebSocket;
import models.User;
import org.eclipse.jetty.websocket.api.Session;
import services.chat.utils.Notify;
import servlets.info.ServerInfo;
import services.chat.utils.History;

import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatService {
    private Set<ChatWebSocket> webSockets;
    private History history = new History();

    public ChatService() {
        this.webSockets = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public void clearHistory() {
        history.clear();
    }

    public void sendMessage(String data) {
        history.add(data);
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
        webSockets.forEach(i -> stringBuilder.append(i.getUser().getLogin()).append('\n'));
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
            sendMessage(Notify.JOIN.execute(currentTime(), login));
        } else {
            sendMessage(Notify.LEFT.execute(currentTime(), login));
        }
    }

    public void messageBanned(String login, boolean isBanned) {
        if (isBanned) {
            sendMessage(Notify.BAN.execute(currentTime(), login));
        } else {
            sendMessage(Notify.UNBAN.execute(currentTime(), login));
        }
    }

    public void regularMessage(String login, String data) {
        sendMessage(Notify.REGULAR.execute(currentTime(), login, data));
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
        return login == null || ServerInfo.getInstance().getUsers() >= ServerInfo.getInstance().getUsersLimit();
    }

    public void add(ChatWebSocket webSocket) {
        webSockets.add(webSocket);
    }

    public void remove(ChatWebSocket webSocket) {
        webSockets.remove(webSocket);
    }
}

