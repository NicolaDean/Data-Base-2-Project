package it.polimi.db2.controllers;

import it.polimi.db2.services.UserServices;
import org.apache.commons.lang.StringEscapeUtils;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "registration", value = "/registration")
public class Registration extends BasicServerlet {

    @EJB(name="it.polimi.db2.services/UserService")
    private UserServices userServices;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = StringEscapeUtils.escapeJava(request.getParameter("username"));
        String password = StringEscapeUtils.escapeJava(request.getParameter("password"));

        this.userServices.createUser(username,password,username+"@gmail.com");
    }
}
