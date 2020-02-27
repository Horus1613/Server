package services.sign;

import crypt.CipherHelper;
import dao.JDBC_UserDAO;
import models.User;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SignServiceTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    JDBC_UserDAO jdbc_userDAO = mock(JDBC_UserDAO.class);

    @Test
    public void signIn_shouldReturnRedirectWhenGoodLogin() throws IOException {
        when(request.getParameter("login")).thenReturn("root");
        when(request.getParameter("password")).thenReturn("toor");
        when(jdbc_userDAO.findByLogin("root")).thenReturn(new User("root", "toor", false));
        ResponseModel model = SignService.signIn(jdbc_userDAO, request);
        assertEquals("/chat.html?user=".concat(CipherHelper.cipher("root")), model.getRedirectUrl());
    }

    @Test
    public void signIn_shouldSetStatus401WhenBadLoginOrPassword() {
        when(request.getParameter("login")).thenReturn("root");
        when(request.getParameter("password")).thenReturn("badpassword");
        when(jdbc_userDAO.findByLogin("root")).thenReturn(new User("root", "toor", false));

        ResponseModel model = SignService.signIn(jdbc_userDAO, request);
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, model.getStatus());

        when(request.getParameter("login")).thenReturn("zxcv");
        when(request.getParameter("password")).thenReturn("vcxz");

        model = SignService.signIn(jdbc_userDAO, request);
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, model.getStatus());
    }

    @Test
    public void signIn_shouldSetStatus400WhenNullLoginOrPassword() {
        when(request.getParameter("login")).thenReturn("root");
        when(request.getParameter("password")).thenReturn(null);
        when(jdbc_userDAO.findByLogin("root")).thenReturn(new User("root", "toor", false));

        ResponseModel model = SignService.signIn(jdbc_userDAO, request);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, model.getStatus());

        when(request.getParameter("login")).thenReturn("zxcv");
        when(request.getParameter("password")).thenReturn(null);

        model = SignService.signIn(jdbc_userDAO, request);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, model.getStatus());
    }

    @Test
    public void signUp_shouldSetStatus409IfUserIsAlreadyExist() {
        when(request.getParameter("login")).thenReturn("root");
        when(request.getParameter("password")).thenReturn("toor");
        when(jdbc_userDAO.findByLogin("root")).thenReturn(new User("root", "toor", false));

        ResponseModel model = SignService.signUp(jdbc_userDAO, request);
        assertEquals(HttpServletResponse.SC_CONFLICT, model.getStatus());
    }

    @Test
    public void signUp_shouldSetStatus200WhenNewUser() {
        when(request.getParameter("login")).thenReturn("new");
        when(request.getParameter("password")).thenReturn("user");
        when(jdbc_userDAO.findByLogin("root")).thenReturn(null);

        ResponseModel model = SignService.signUp(jdbc_userDAO, request);
        assertEquals(HttpServletResponse.SC_OK, model.getStatus());
    }

    @Test
    public void signUp_shouldSetStatus400WhenEmptyLoginOrPassword() {
        when(request.getParameter("login")).thenReturn("");
        when(request.getParameter("password")).thenReturn("pass");
        when(jdbc_userDAO.findByLogin("root")).thenReturn(null);

        ResponseModel model = SignService.signUp(jdbc_userDAO, request);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, model.getStatus());

        when(request.getParameter("login")).thenReturn("root");
        when(request.getParameter("password")).thenReturn("");

        model = SignService.signUp(jdbc_userDAO, request);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, model.getStatus());
    }
}