package it.polimi.db2;

import com.google.gson.Gson;
import it.polimi.db2.entitys.OptionalProduct;
import it.polimi.db2.entitys.Package;
import it.polimi.db2.entitys.Service;
import it.polimi.db2.entitys.ServiceTypes.MobilePhoneServices;
import it.polimi.db2.exception.AuthenticationFailed;
import it.polimi.db2.exception.NotUniqueUsername;
import it.polimi.db2.services.UserServices;

import java.io.*;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    @EJB(name="it.polimi.db2.Services/UserServices")
    private UserServices userServices;
    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        response.setContentType("text/html");

        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("default" );

        EntityManager entitymanager = emfactory.createEntityManager( );

        Gson gson = new Gson();


        Query students = entitymanager.createQuery("select p from OptionalProduct p");
        Query services = entitymanager.createQuery("select s from Service s");
        Query packages = entitymanager.createQuery("select p from Package p");


        List<Service> resultServices = services.getResultList();



        System.out.println("SIZEE:"+resultServices.size());
        List<OptionalProduct> products = students.getResultList();

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        for(OptionalProduct p:products)
        {
            out.println("<h2> "+p.getName()+"</h2>");
        }

        out.println("</body></html>");

        for(Service x:resultServices)
        {
            out.println("<h2> "+gson.toJson(x)+"</h2>");
        }
        try {
            List<Package> resultPackages = packages.getResultList();
            for(Package x:resultPackages)
            {
                out.println("<h2> "+gson.toJson(x)+"</h2>");
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        MobilePhoneServices tmp = new MobilePhoneServices();
        tmp.setExtraMinutesFee(0.5);
        tmp.setExtraSMSFee(0.3);
        tmp.setMinutes(2000);
        tmp.setSms(450);
// NON PERSISTE

        try {
            entitymanager.persist(tmp);
            resultServices = services.getResultList();
            System.out.println("<h2>SIZEE:"+resultServices.size()+"</h2>");
            entitymanager.close();
            emfactory.close();
            //entitymanager.getTransaction().commit();
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        System.out.println("babbani");

    }



    public void destroy() {
    }
}