package websockets.chat;

import crypt.CipherHelper;
import dao.UserDAO;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "WebSocketChatServlet", urlPatterns = "/chat?user=")
public class WebSocketChatServlet extends WebSocketServlet {

    private UserDAO userDAO;
    private final static int LOGOUT_TIME = 10 * 60 * 1000;
    private final ChatService chatService;

    public WebSocketChatServlet(UserDAO userDAO) {
        this.chatService = new ChatService();
        this.userDAO = userDAO;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator((req, resp) -> new ChatWebSocket(chatService, userDAO,
                CipherHelper.decipher(req.getParameterMap().get("user").get(0))));
    }
}
