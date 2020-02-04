package servlets.signs;

import accounts.AccountService;
import accounts.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignUpServlet extends HttpServlet {
    private final AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserProfile userProfile = new UserProfile(req.getParameter("login"),req.getParameter("password"),"default@mail.ru");
        if(!accountService.hasProfileByLogin(userProfile)){
            accountService.addNewUser(userProfile);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Signed up");
        } else{
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().println("User is already signed up");
        }

    }
}
