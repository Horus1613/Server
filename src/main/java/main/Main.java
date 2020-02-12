package main;

import dao.HibernateUserDAO;
import dao.UserDAO;
import dao.JdbcUserDAO;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.MirrorServlet;
import servlets.signs.SignInServlet;
import servlets.signs.SignUpServlet;

public class Main {
    public static void main(String[] args) throws Exception {

        UserDAO userDao = new HibernateUserDAO();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new MirrorServlet()),"/mirror");
        context.addServlet(new ServletHolder(new SignUpServlet(userDao)),"/signup");
        context.addServlet(new ServletHolder(new SignInServlet(userDao)),"/signin");
        

        Server server = new Server(8080);
        server.setHandler(context);
        server.start();
        System.out.println("Server started");
        server.join();
    }
}
