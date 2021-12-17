package it.polimi.db2.controllers;

import it.polimi.db2.entitys.User;
import it.polimi.db2.exception.ElementNotFound;
import it.polimi.db2.services.UserServices;
import it.polimi.db2.utils.TemplatePathManager;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "profile", value = "/profile")


public class Profile extends BasicServerlet{
    @EJB(name="it.polimi.db2.services/UserService")
    private UserServices userServices;


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user  = (User) session.getAttribute("user") ;
        try {
            user=userServices.getUserById(user.getId());
            session.setAttribute("user",user);
        } catch (ElementNotFound elementNotFound) {
            elementNotFound.printStackTrace();
        }
        request.setAttribute("user", user);
        request.setAttribute("register", TemplatePathManager.registration);
        request.setAttribute("log in", TemplatePathManager.loginPage);

        this.templateRenderer(request,response, TemplatePathManager.profile);

    }
}
