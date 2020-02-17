package websockets.chat;

import dao.UserDAO;
import models.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websockets.utils.CommandPatterns;

@SuppressWarnings("UnusedDeclaration")
@WebSocket
public class ChatWebSocket {
    private static final String CLEAR_COMMAND = "/clear";

    private ChatService chatService;
    private Session session;
    private UserDAO userDAO;
    private boolean adminMode;
    private boolean loginError = false;
    private CommandPatterns commandPatterns;
    private String login;
    User user;

    public ChatWebSocket(ChatService chatService, UserDAO userDAO, String login) {
        this.loginError = chatService.loginError(login);
        if (loginError) return;
        this.chatService = chatService;
        this.userDAO = userDAO;
        this.user = this.userDAO.findByLogin(login);
        this.login = login;
        this.adminMode = user.getLogin().equals("root");
        this.commandPatterns = new CommandPatterns();
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        if (loginError) {
            session.close();
            return;
        }
        chatService.add(this);
        this.session = session;
        chatService.printHistory(this.session);
        chatService.messageConnectivity(user.getLogin(), true);
        chatService.updateOnline();
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        int patternIndex = commandPatterns.patternValidate(data);
        if (adminMode && patternIndex >= 0) {
            String commandUsername;
            switch (patternIndex) {
                case 0:
                    commandUsername = commandPatterns.userName(data, patternIndex);
                    userDAO.banControlByLogin(commandUsername, true);
                    chatService.messageBanned(commandUsername, true);
                    break;
                case 1:
                    commandUsername = commandPatterns.userName(data, patternIndex);
                    userDAO.banControlByLogin(commandUsername, false);
                    chatService.messageBanned(commandUsername, false);
                    break;
                case 2:
                    chatService.sendMessage(CLEAR_COMMAND, true);
                    chatService.history.clear();
                    break;
            }
        } else {
            chatService.regularMessage(user.getLogin(), data);
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        if (loginError) return;
        chatService.remove(this);
        chatService.messageConnectivity(user.getLogin(), false);
        chatService.updateOnline();
    }

    public void sendString(String data) {
        try {
            session.getRemote().sendString(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}