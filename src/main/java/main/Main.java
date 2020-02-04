package main;

import accounts.AccountService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.MirrorServlet;
import servlets.signs.SignInServlet;
import servlets.signs.SignUpServlet;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {
        AccountService accountService = new AccountService();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new MirrorServlet()),"/mirror");
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)),"/signup");
        context.addServlet(new ServletHolder(new SignInServlet(accountService)),"/signin");
        

        Server server = new Server(8080);
        server.setHandler(context);
        server.start();
        System.out.println("Server started");
        server.join();
    }
}
