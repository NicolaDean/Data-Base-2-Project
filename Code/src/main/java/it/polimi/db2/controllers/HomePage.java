package it.polimi.db2.controllers;

import it.polimi.db2.entitys.Package;
import it.polimi.db2.services.PackageService;
import it.polimi.db2.utils.TemplatePathManager;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "home", value = "/home")
public class HomePage extends BasicServerlet {
    @EJB(name="it.polimi.db2.services/PackageService")
    private PackageService packageService;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<Package> packages = this.packageService.getAllPackages();

        request.setAttribute("packages",packages);
        
      //Setting Name if logged
        HttpSession session=request.getSession(false);
        if(session!=null){
            String name=(String)session.getAttribute("name");
            request.setAttribute("name",name);
        }

        this.templateRenderer(request,response, TemplatePathManager.homePage);
    }
}
