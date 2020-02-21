package servlets.signs;

import dao.JDBC_UserDAO;
import dao.UserDAO;
import models.User;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SignUpServletTest {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    JDBC_UserDAO jdbc_userDAO = mock(JDBC_UserDAO.class);
    SignUpServlet signUpServlet = new SignUpServlet(jdbc_userDAO);

    public SignUpServletTest() throws SQLException, ClassNotFoundException {
    }

    @Test
    public void userIsAlreadyExist() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getParameter("login")).thenReturn("root");
        when(request.getParameter("password")).thenReturn("toor");
        when(jdbc_userDAO.findByLogin("root")).thenReturn(new User("root","toor",false));
        when(response.getWriter()).thenReturn(writer);

        signUpServlet.doPost(request,response);

        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @Test
    public void newUser() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getParameter("login")).thenReturn("new");
        when(request.getParameter("password")).thenReturn("user");
        when(jdbc_userDAO.findByLogin("root")).thenReturn(null);
        when(response.getWriter()).thenReturn(writer);

        signUpServlet.doPost(request,response);

        verify(jdbc_userDAO).save(any());
    }

    @Test
    public void emptyLoginOrPassword() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getParameter("login")).thenReturn("");
        when(request.getParameter("password")).thenReturn("pass");
        when(response.getWriter()).thenReturn(writer);

        signUpServlet.doPost(request,response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }
}