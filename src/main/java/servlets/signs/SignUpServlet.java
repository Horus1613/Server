package servlets.signs;

import dao.UserDAO;
import models.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignUpServlet extends HttpServlet {
    private final UserDAO userDao;

    public SignUpServlet(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = new User();
        user.setLogin(req.getParameter("login"));
        user.setPassword(req.getParameter("password"));
        if(userDao.findByLogin(req.getParameter("login"))==null){
            userDao.save(user);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Signed up");
        } else{
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().println("User is already signed up");
        }

    }
}
