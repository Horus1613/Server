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
        boolean hasLogin = false;
        boolean hasPassword = false;
        try {
            hasLogin = !req.getParameter("login").isEmpty();
            hasPassword = !req.getParameter("password").isEmpty();
        } catch (NullPointerException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Bad login or password");
            return;
        }
        User user = new User(req.getParameter("login"), req.getParameter("password"));
        if (hasLogin && hasPassword && userDao.findByLogin(req.getParameter("login")) == null) {
            userDao.save(user);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Signed up");

        } else {
            if (hasLogin && hasPassword) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().println("User is already signed up");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Bad login or password");
            }

        }

    }
}
