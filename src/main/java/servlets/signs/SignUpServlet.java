package servlets.signs;

import dao.UserDAO;
import models.User;
import services.sign.ResponseModel;
import services.sign.SignService;

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
        ResponseModel responseModel = SignService.signUp(userDao,req);
        resp.setStatus(responseModel.getStatus());
        resp.getWriter().println(responseModel.getResponseData());
    }
}
