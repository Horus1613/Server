package main;

import dao.JDBC_UserDAO;
import dao.UserDAO;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import services.resources.ResourceService;
import services.resources.ResourceServiceMBean;
import servlets.MirrorServlet;
import servlets.ResourceServlet;
import servlets.info.ServerInfo;
import servlets.info.ServerInfoServlet;
import servlets.signs.SignInServlet;
import servlets.signs.SignUpServlet;
import servlets.websockets.WebSocketChatServlet;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import java.lang.management.ManagementFactory;

public class Main {
    public static void main(String[] args) throws Exception {

        UserDAO userDao = new JDBC_UserDAO();

        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("Admin:type=UsersInfo");
        mBeanServer.registerMBean(ServerInfo.getInstance(),name);

        ResourceService resourceService = new ResourceService();
        ObjectName resourceServiceName = new ObjectName("Admin:type=ResourceServerController");
        StandardMBean resourceServiceMBean = new StandardMBean(resourceService, ResourceServiceMBean.class);
        mBeanServer.registerMBean(resourceServiceMBean, resourceServiceName);



        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new MirrorServlet()), "/mirror");
        context.addServlet(new ServletHolder(new SignUpServlet(userDao)), "/signup");
        context.addServlet(new ServletHolder(new SignInServlet(userDao)), "/signin");
        context.addServlet(new ServletHolder(new ServerInfoServlet()), "/info");
        context.addServlet(new ServletHolder(new ResourceServlet(resourceService)),"/resources");

        context.addServlet(new ServletHolder(new WebSocketChatServlet(userDao)), "/chat");


        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setBaseResource(Resource.newResource("src\\main\\resources\\public_html\\"));

        HandlerList handlerList = new HandlerList();
        handlerList.setHandlers(new Handler[]{resourceHandler,context});

        Server server = new Server(8080);
        server.setHandler(handlerList);
        server.start();
        System.out.println("Server started");
        server.join();

    }
}
