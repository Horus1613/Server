package servlets.signs;

import crypt.CipherHelper;
import dao.JDBC_UserDAO;
import dao.UserDAO;
import models.User;
import org.junit.Test;
import servlets.MirrorServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SignInServletTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    JDBC_UserDAO jdbc_userDAO = mock(JDBC_UserDAO.class);
    SignInServlet signInServlet = new SignInServlet(jdbc_userDAO);

    @Test
    public void goodLogin() throws IOException {
        when(request.getParameter("login")).thenReturn("root");
        when(request.getParameter("password")).thenReturn("toor");
        when(jdbc_userDAO.findByLogin("root")).thenReturn(new User("root","toor",false));
        signInServlet.doPost(request,response);

        verify(response).sendRedirect(any());


    }

    @Test
    public void badLoginOrPassword() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getParameter("login")).thenReturn("root");
        when(request.getParameter("password")).thenReturn("badpassword");
        when(jdbc_userDAO.findByLogin("root")).thenReturn(new User("root","toor",false));
        when(response.getWriter()).thenReturn(writer);

        signInServlet.doPost(request,response);

        when(request.getParameter("login")).thenReturn("zxcv");
        when(request.getParameter("password")).thenReturn("vcxz");

        signInServlet.doPost(request,response);
        verify(response,times(2)).setStatus(401);
    }

    @Test
    public void nullLoginOrPassword() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getParameter("login")).thenReturn(null);
        when(request.getParameter("password")).thenReturn("pass");
        when(response.getWriter()).thenReturn(writer);

        signInServlet.doPost(request,response);

        when(request.getParameter("login")).thenReturn("login");
        when(request.getParameter("password")).thenReturn(null);

        signInServlet.doPost(request,response);
        verify(response, times(2)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }


}