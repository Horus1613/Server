package websockets.chat;

import models.User;
import org.junit.Test;
import websockets.utils.History;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ChatServiceTest {
    ChatWebSocket chatWebSocket = mock(ChatWebSocket.class);
    Set<ChatWebSocket> webSockets;
    Set<User> usersOnline;
    History history = mock(History.class);
    ChatService chatService = new ChatService();

    public ChatServiceTest() {
        usersOnline = Collections.newSetFromMap(new ConcurrentHashMap<>());
        webSockets = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    @Test
    public void sendMessage() {


        Iterator mockIterator = mock(Iterator.class);
        doCallRealMethod().when(webSockets).forEach(any(Consumer.class));
        when(webSockets.iterator()).thenReturn(mockIterator);
        when(mockIterator.hasNext()).thenReturn(true, false);
        when(mockIterator.next()).thenReturn(chatWebSocket);

        chatService.sendMessage("test",false);

        verify(chatWebSocket).sendString(any());
    }

    @Test
    public void updateOnline() {
    }

    @Test
    public void messageConnectivity() {
    }

    @Test
    public void messageBanned() {
    }

    @Test
    public void regularMessage() {
    }

    @Test
    public void printHistory() {
    }

    @Test
    public void loginError() {
    }

    @Test
    public void add() {
    }

    @Test
    public void remove() {
    }
}