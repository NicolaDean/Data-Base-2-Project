package it.polimi.db2.controllers;

import it.polimi.db2.entitys.User;
import it.polimi.db2.exception.RegistrationFailed;
import it.polimi.db2.services.UserServices;
import it.polimi.db2.utils.TemplatePathManager;
import org.apache.commons.lang.StringEscapeUtils;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.GenericSignatureFormatError;

@WebServlet(name = "registration", value = "/registration")
public class Registration extends BasicServerlet {

    @EJB(name="it.polimi.db2.services/UserService")
    private UserServices userServices;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = null;
        String password = null;
        String email = null;

        System.out.println("User Registration");

        username = StringEscapeUtils.escapeJava(request.getParameter("username"));
        password = StringEscapeUtils.escapeJava(request.getParameter("password"));
        email = StringEscapeUtils.escapeJava(request.getParameter("email"));

        System.out.println(username+", "+password+", "+email+";");

        try {
            this.userServices.createUser(username,password,email);
            this.templateRenderer(request,response, TemplatePathManager.loginPage);
            return;

        }catch (RegistrationFailed registrationFailed){
            this.setError(request,response,"Registration failed. Check if all forms are filled! ", TemplatePathManager.registration);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user!=null)
        {
            session.invalidate();
            this.setError(request,response,"To let you register, you have been logged out", TemplatePathManager.registration);
        }
        else this.templateRenderer(request,response, TemplatePathManager.registration);
    }

    public void destroy() {
    }
}
