package services.chat;

import dao.UserDAO;
import models.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import servlets.info.ServerInfo;
import services.chat.utils.CommandPatterns;
import services.chat.utils.Commands;
import services.chat.ChatService;

@SuppressWarnings("UnusedDeclaration")
@WebSocket
public class ChatWebSocket {
    private static final String ADMIN_LOGIN = "root";

    private ChatService chatService;
    private Session session;
    private UserDAO userDAO;
    private boolean adminMode;
    private boolean loginError = false;
    private CommandPatterns commandPatterns;
    private String login;
    private User user;

    public User getUser() {
        return user;
    }

    public ChatWebSocket(ChatService chatService, UserDAO userDAO, String login) {
        try {
            this.loginError = chatService.loginError(login);
            this.chatService = chatService;
            this.userDAO = userDAO;
            this.user = this.userDAO.findByLogin(login);
            this.login = login;
            this.adminMode = user.getLogin().equals(ADMIN_LOGIN);
            this.commandPatterns = new CommandPatterns();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        if (loginError) {
            session.close();
            return;
        }
        chatService.add(this);
        ServerInfo.getInstance().addUser();
        this.session = session;
        chatService.printHistory(this.session);
        chatService.messageConnectivity(user.getLogin(), true);
        chatService.updateOnline();
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        if (adminMode && commandPatterns.validatePattern(data)) {
            commandPatterns.sendCommand(userDAO,chatService,data);
        } else {
            chatService.regularMessage(user.getLogin(), data);
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        if (loginError) return;
        chatService.remove(this);
        ServerInfo.getInstance().removeUser();
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