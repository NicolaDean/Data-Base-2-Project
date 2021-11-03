package it.polimi.db2.controllers;

import it.polimi.db2.exception.RegistrationFailed;
import it.polimi.db2.services.UserServices;
import it.polimi.db2.utils.TemplatePathManager;
import org.apache.commons.lang.StringEscapeUtils;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        }catch (GenericSignatureFormatError registrationFailed){
        }
    }


    public void destroy() {
    }
}
