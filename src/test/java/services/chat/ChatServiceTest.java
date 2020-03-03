package services.chat;

import models.User;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ChatServiceTest {
    ChatWebSocket chatWebSocket = mock(ChatWebSocket.class);
    ChatService chatService = new ChatService();


    @Test
    public void shouldSendDataForAllUsers() {
        chatService.add(chatWebSocket);
        chatService.sendMessage("test");

        verify(chatWebSocket).sendString(any());
    }

    @Test
    public void updateOnline() {
        when(chatWebSocket.getUser()).thenReturn(new User("root", "toor", false));
        chatService.add(chatWebSocket);
        chatService.updateOnline();
        verify(chatWebSocket).sendString("!uroot\n");
    }

    @Test
    public void messageConnectivity() {
        chatService.add(chatWebSocket);
        String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

        chatService.messageConnectivity("root", true);
        verify(chatWebSocket).sendString(String.format("%s\t\tUser %s joined!", time, "root"));

        chatService.messageConnectivity("root", false);
        verify(chatWebSocket).sendString(String.format("%s\t\tUser %s left!", time, "root"));
    }

    @Test
    public void messageBanned() {
        chatService.add(chatWebSocket);
        String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

        chatService.messageBanned("root", true);
        verify(chatWebSocket).sendString(String.format("%s\t\tUser %s banned!", time, "root"));

        chatService.messageBanned("root", false);
        verify(chatWebSocket).sendString(String.format("%s\t\tUser %s unbanned!", time, "root"));
    }

    @Test
    public void regularMessage() {
        chatService.add(chatWebSocket);
        String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        chatService.regularMessage("root", "message");
        verify(chatWebSocket).sendString(String.format("%s\t\t%s:%s", time, "root", "message"));
    }

    @Test
    public void printHistory() throws IOException {
        Session session = mock(Session.class);
        RemoteEndpoint remoteEndpoint = mock(RemoteEndpoint.class);

        when(session.getRemote()).thenReturn(remoteEndpoint);

        chatService.add(chatWebSocket);
        chatService.printHistory(session);

        verify(session.getRemote()).sendString(any());
    }
}