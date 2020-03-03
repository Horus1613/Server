package servlets;

import resources.TestResource;
import sax.ReadXMLbySAX;
import services.resources.ResourceService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResourceServlet extends HttpServlet {
    private final ResourceService resourceService;

    public ResourceServlet(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        String path = req.getParameter("path");
        resp.setContentType("text/html;charset=utf-8");

        if (path == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        TestResource resource = (TestResource) ReadXMLbySAX.readXML(path);
        resourceService.setResource(resource);

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
