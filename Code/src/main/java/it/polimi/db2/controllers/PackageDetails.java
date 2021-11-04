package it.polimi.db2.controllers;

import it.polimi.db2.entitys.OptionalProduct;
import it.polimi.db2.entitys.Package;
import it.polimi.db2.exception.NoPackageFound;
import it.polimi.db2.services.PackageService;
import it.polimi.db2.utils.TemplatePathManager;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "package-details", value = "/package-details")
public class PackageDetails extends BasicServerlet{
    @EJB(name="it.polimi.db2.services/PackageService")
    private PackageService packageService;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String input =  request.getParameter("id");
        int id = 0;

        try{
            id = Integer.parseInt(input);
        }catch (Exception e)
        {
            //TODO print error or redirect to Home with error (not valid id)
        }

        try {
            Package p = this.packageService.getPackageById(id);
            request.setAttribute("package",p);
            List<OptionalProduct> optionalProducts= p.getOptionalProducts();
            request.setAttribute("optionalProducts",optionalProducts);
            this.templateRenderer(request,response, TemplatePathManager.packageDetails);

        } catch (NoPackageFound e) {
            e.printStackTrace();
            //TODO print error with "no package found"
        }




    }
}
