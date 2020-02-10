package servlets.signs;

import dao.UserDAO;
import models.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignInServlet extends HttpServlet {
    private final UserDAO userDao;

    public SignInServlet(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        User user = userDao.findByLogin(login);
        if(user!=null && user.getPassword().equals(req.getParameter("password"))){
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Authorized: "+ login);
        } else {
            resp.setStatus(401);
            resp.getWriter().println("Unauthorized");
        }
    }
}
