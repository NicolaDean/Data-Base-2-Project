package it.polimi.db2;

import it.polimi.db2.Entitys.OptionalProduct;

import java.io.*;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        response.setContentType("text/html");

        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("default" );

        EntityManager entitymanager = emfactory.createEntityManager( );


        Query students = entitymanager.createQuery("select p from OptionalProduct p");

        List<OptionalProduct> products = students.getResultList();

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        for(OptionalProduct p:products)
        {
            out.println("<h2> "+p.getName()+"</h2>");
        }
        out.println("</body></html>");
    }

    public void destroy() {
    }
}