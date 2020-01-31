package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.HelloServlet;

import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {
        HelloServlet helloServlet = new HelloServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(helloServlet),"/mirror");

        Server server = new Server(8080);
        server.setHandler(context);
        server.start();
        System.out.println("Server started");
        server.join();
    }
}
