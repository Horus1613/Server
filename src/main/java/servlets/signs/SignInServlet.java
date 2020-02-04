package servlets.signs;

import accounts.AccountService;
import accounts.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignInServlet extends HttpServlet {
    private final AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserProfile userProfile=accountService.getUserByLogin(req.getParameter("login"));
        if(userProfile!=null && userProfile.getPass().equals(req.getParameter("password"))){
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Authorized: "+userProfile.getLogin());
        } else {
            resp.setStatus(401);
            resp.getWriter().println("Unauthorized");
        }
    }
}
