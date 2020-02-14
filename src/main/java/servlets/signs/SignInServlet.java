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
        String login = null;
        String password = null;
        try {
            login = req.getParameter("login");
            password = req.getParameter("password");
            if (login == null || password == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Bad login or password");
                return;
            }
        } catch (NullPointerException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Bad login or password");
            return;
        }

        User user = userDao.findByLogin(login);
        if (!login.isEmpty() && !password.isEmpty() && user != null && !user.getBanned()
                && user.getPassword().equals(password)) {
            resp.setStatus(HttpServletResponse.SC_FOUND);
            resp.sendRedirect("/chat.html?user=".concat(login));
        } else {
            resp.setStatus(401);
            resp.getWriter().println("Unauthorized");
        }
    }
}
