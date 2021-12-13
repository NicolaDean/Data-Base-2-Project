package it.polimi.db2.controllers;


import it.polimi.db2.entitys.Order;
import it.polimi.db2.entitys.User;
import it.polimi.db2.exception.AuthenticationFailed;
import it.polimi.db2.exception.NotUniqueUsername;
import it.polimi.db2.exception.WrongCredential;
import it.polimi.db2.services.UserServices;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import it.polimi.db2.utils.TemplatePathManager;
import org.apache.commons.lang.StringEscapeUtils;

@WebServlet(name = "login", value = "/login")
public class CheckLogin extends BasicServerlet {

    @EJB(name="it.polimi.db2.services/UserService")
    private UserServices userServices;


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = null;
        String password = null;

        username = StringEscapeUtils.escapeJava(request.getParameter("username"));
        password = StringEscapeUtils.escapeJava(request.getParameter("password"));

        try {
            User user = userServices.checkAuthentication(username,password);

            HttpSession session=request.getSession();
            session.setAttribute("user",user);

            //Check presence of pending order in session
            Order order = (Order) session.getAttribute("order");

          if(user.getType().equals("user"))
          {
              if (order != null) {
                  response.sendRedirect("package-details?id=" + order.getPackage().getId());
              } else {
                  response.sendRedirect("home");
              }
              return;
          }
          else if(user.getType().equals("admin"))
          {
              response.sendRedirect("admin");
          }
        } catch (WrongCredential wrongCredential) {
            this.setError(request,response,"Wrong credentials!", TemplatePathManager.loginPage);
        }catch (AuthenticationFailed e) {
            this.setError(request,response,"Authentication failed", TemplatePathManager.loginPage);
        } catch (NotUniqueUsername e) {
            this.setError(request,response,"Internal error occurred, please try again", TemplatePathManager.loginPage);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            if (user.getType().equals("user")) {
                    response.sendRedirect("home");
            } else if (user.getType().equals("admin")) {
                response.sendRedirect("admin");
            }
        }
        else {
            this.templateRenderer(request,response, TemplatePathManager.loginPage);
        }

    }

    public void destroy() {
    }
}
