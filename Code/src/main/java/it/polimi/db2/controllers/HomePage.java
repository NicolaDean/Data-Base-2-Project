package it.polimi.db2.controllers;

import com.google.gson.Gson;
import it.polimi.db2.entitys.Package;
import it.polimi.db2.services.PackageService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "home", value = "/home")
public class HomePage extends BaseServerlet{
    @EJB(name="it.polimi.db2.services/PackageService")
    private PackageService packageService;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");

        List<Package> packages = this.packageService.getAllPackages();
        for(Package p:packages)
        {
            Gson g = new Gson();
            System.out.println(g.toJson(p));
            out.println("<h2>"  + g.toJson(p) + "</h2>");
        }
        out.println("</html></body>");
    }
}
