package services.sign;

import crypt.CipherHelper;
import dao.UserDAO;
import models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignService {

    public static ResponseModel signIn(UserDAO userDAO, HttpServletRequest req){
        ResponseModel resp = new ResponseModel();
        String login;
        String password;
        try {
            login = req.getParameter("login");
            password = req.getParameter("password");
            if (login == null || password == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setResponseData("Bad login or password");
                return resp;
            }
        } catch (NullPointerException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setResponseData("Bad login or password");
            return resp;
        }

        User user = userDAO.findByLogin(login);
        if (!login.isEmpty() && !password.isEmpty() && user != null && !user.getBanned()
                && user.getPassword().equals(password)) {
            resp.setStatus(HttpServletResponse.SC_FOUND);
            try {
                resp.setRedirectUrl("/chat.html?user=".concat(CipherHelper.cipher(login)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            resp.setStatus(401);
            resp.setRedirectUrl("/chat.html?user=".concat(CipherHelper.cipher(login)));
        }
        return resp;
    }

    public static ResponseModel signUp(UserDAO userDAO, HttpServletRequest req){
        ResponseModel resp = new ResponseModel();

        boolean hasLogin = false;
        boolean hasPassword = false;
        try {
            hasLogin = !req.getParameter("login").isEmpty();
            hasPassword = !req.getParameter("password").isEmpty();
        } catch (NullPointerException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setResponseData("Bad login or password");
            return resp;
        }
        User user = new User(req.getParameter("login"), req.getParameter("password"),false);
        if (hasLogin && hasPassword && userDAO.findByLogin(req.getParameter("login")) == null) {
            userDAO.save(user);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setResponseData("Signed up");
        } else {
            if (hasLogin && hasPassword) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.setResponseData("User is already signed up");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setResponseData("Bad login or password ");
            }
        }
        return resp;
    }

}
