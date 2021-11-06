package it.polimi.db2.controllers;


import it.polimi.db2.entitys.*;
import it.polimi.db2.entitys.Package;
import it.polimi.db2.exception.ElementNotFound;
import it.polimi.db2.exception.NoPackageFound;
import it.polimi.db2.services.ContractServices;
import it.polimi.db2.services.PackageService;
import it.polimi.db2.services.UserServices;
import it.polimi.db2.utils.TemplatePathManager;
import org.apache.commons.lang.StringEscapeUtils;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.text.html.CSS.getAttribute;

@WebServlet(name = "contract", value = "/contract")
public class Contract extends BasicServerlet {


    @EJB(name = "it.polimi.db2.services/ContractServices")
    private ContractServices contractServices;
    @EJB(name = "it.polimi.db2.services/PackageService")
    private PackageService packageService;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Order order = new Order();

        String rateId;
        rateId = StringEscapeUtils.escapeJava(request.getParameter("rates"));
        String packageId;
        packageId = StringEscapeUtils.escapeJava(request.getParameter("packId"));
        Package p = null;
        request.setAttribute("rate", rateId);
        try {
            p = this.packageService.getPackageById(Integer.parseInt(packageId));
            request.setAttribute("package", p);
        } catch (NoPackageFound noPackageFound) {
            noPackageFound.printStackTrace();
        }





        //Adding Optional Product
        try {
            String[]  optionalProducts;
            if(request.getParameterValues("optionalProducts")!=null){
                  optionalProducts = request.getParameterValues("optionalProducts");
                request.setAttribute("optionalProducts", this.contractServices.convertOptionalProducts(optionalProducts) );
            }
            else{
                //TODO check if putting one for no optional product is OK

                 optionalProducts = new String[1];
                 optionalProducts[0]="1";

            }
            //FIND LOGGED USER
            HttpSession session = request.getSession();
            if(session.getAttribute("user")!=null){
                User u = (User) session.getAttribute("user");
                this.contractServices.createContract(u.getId(), Integer.parseInt(packageId), Integer.parseInt(rateId), optionalProducts);
                this.templateRenderer(request, response, TemplatePathManager.contract);

            }
            else {
                this.setError(request,response,"To continue the purchase you need to log in!", TemplatePathManager.loginPage);
            }

        } catch (ElementNotFound e) {
            e.printStackTrace();
            this.setError(request,response,"Offers not found", TemplatePathManager.error);
        }

    }
}

