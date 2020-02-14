package websockets.chat;

import dao.UserDAO;
import models.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("UnusedDeclaration")
@WebSocket
public class ChatWebSocket {
    private ChatService chatService;
    private Session session;
    private UserDAO userDAO;
    private User user;
    private boolean adminMode;
    private ArrayList<Pattern> patterns = new ArrayList<>();
    private final Pattern banCommandPattern = Pattern.compile("^\\/auth ([\\S]+).*");

    String login;

    public ChatWebSocket(ChatService chatService, UserDAO userDAO, String login) {
        this.chatService = chatService;
        this.userDAO = userDAO;
        user = this.userDAO.findByLogin(login);
        this.login=login;
        adminMode = user.getLogin().equals("root");

        patterns.add(Pattern.compile("^/ban ([\\S]+).*"));
        patterns.add(Pattern.compile("^/unban ([\\S]+).*"));
        patterns.add(Pattern.compile("/clear -global"));
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        chatService.add(this);
        this.session = session;
        chatService.history.forEach(this::sendString);
        chatService.sendMessage(currentTime() + user.getLogin() + " joined!",false);
        chatService.updateOnline();
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        int patternIndex = patternValidate(data);
        if(adminMode && patternIndex>=0){
            Matcher matcher = patterns.get(patternIndex).matcher(data);
            matcher.find();
            switch (patternIndex){
                case 0:
                    userDAO.banControlByLogin(matcher.group(1),true);
                    chatService.sendMessage(currentTime() + "User "+matcher.group(1)+" was banned!",false);
                    break;
                case 1:
                    userDAO.banControlByLogin(matcher.group(1),false);
                    chatService.sendMessage(currentTime() + "User "+matcher.group(1)+" was unbanned!",false);
                    break;
                case 2:
                    chatService.sendMessage("/clear",true);
                    chatService.history.clear();
                    break;
            }
        } else{
            chatService.sendMessage(currentTime() + data,false);
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        chatService.remove(this);
        chatService.sendMessage(currentTime() + user.getLogin() + " left!",false);
        chatService.updateOnline();
    }

    public void sendString(String data) {
        try {
            session.getRemote().sendString(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String currentTime() {
        String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        return time + "\t\t";
    }

    private int patternValidate(String data){
        for(int i=0;i<patterns.size();i++){
            if(patterns.get(i).matcher(data).matches()){
                return i;
            }
        }
        return -1;
    }
}