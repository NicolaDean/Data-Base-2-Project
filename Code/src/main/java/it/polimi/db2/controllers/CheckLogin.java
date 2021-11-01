package it.polimi.db2.controllers;


import com.google.gson.Gson;
import it.polimi.db2.entitys.Package;
import it.polimi.db2.exception.AuthenticationFailed;
import it.polimi.db2.exception.NotUniqueUsername;
import it.polimi.db2.exception.WrongCredential;
import it.polimi.db2.services.UserServices;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import it.polimi.db2.utils.TemplatePathManager;
import org.apache.commons.lang.StringEscapeUtils;

@WebServlet(name = "login", value = "/login")
public class CheckLogin extends BaseServerlet {

    @EJB(name="it.polimi.db2.services/UserService")
    private UserServices userServices;


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = null;
        String password = null;

        username = StringEscapeUtils.escapeJava(request.getParameter("username"));
        password = StringEscapeUtils.escapeJava(request.getParameter("password"));

        try {
            userServices.checkAuthentication(username,password);
            this.templateRenderer(request,response,TemplatePathManager.homePage);
            return;
        } catch (WrongCredential wrongCredential) {
            this.setError(request,response,"wrong credential bro", TemplatePathManager.loginPage);
        }catch (AuthenticationFailed e) {
            e.printStackTrace();
            this.setError(request,response,"Authentication failed", TemplatePathManager.loginPage);
            return;
        } catch (NotUniqueUsername e) {
            e.printStackTrace();
        }

    }
    public void destroy() {
    }
}
