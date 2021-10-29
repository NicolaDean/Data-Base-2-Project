package it.polimi.db2.Controllers;


import it.polimi.db2.Entitys.OptionalProduct;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "login", value = "/login")
public class CheckLogin extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //DO LOGIN STUFF ( query Database -> create a "NamedQuery" as in prof example" )
    }
    public void destroy() {
    }
}
