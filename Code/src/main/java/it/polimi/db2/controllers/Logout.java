package it.polimi.db2.controllers;

import it.polimi.db2.utils.TemplatePathManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "logout", value = "/logout")
public class Logout extends BasicServerlet{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session=request.getSession();
        session.invalidate();
        this.setError(request,response,"You are successfully logged out!", TemplatePathManager.loginPage);
    }
}
