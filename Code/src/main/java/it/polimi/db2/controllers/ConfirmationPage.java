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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.swing.text.html.CSS.getAttribute;

@WebServlet(name = "confirmation", value = "/confirmation")
public class ConfirmationPage extends BasicServerlet {


    @EJB(name = "it.polimi.db2.services/ContractServices")
    private ContractServices contractServices;

    @EJB(name="it.polimi.db2.services/PackageService")
    private PackageService packageService;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String rateId;

        rateId = StringEscapeUtils.escapeJava(request.getParameter("rates"));
        String packageId;
        packageId = StringEscapeUtils.escapeJava(request.getParameter("packId"));

        int pkgId = Integer.parseInt(packageId);
        String rawData = request.getParameter("startDate");
        Date startingDate = null;
        try {
            startingDate = new SimpleDateFormat("yyyy-mm-dd").parse(rawData);
        } catch (ParseException e) {
            e.printStackTrace();

            try {
                request.setAttribute("package",packageService.getPackageById(pkgId));
            } catch (NoPackageFound ex) {
                ex.printStackTrace();
            }
            this.setError(request,response,"Invalid Date",TemplatePathManager.packageDetails);
            return;
        }

        if(startingDate == null){
            try {
                request.setAttribute("package",packageService.getPackageById(pkgId));
            } catch (NoPackageFound ex) {
                ex.printStackTrace();
            }
            setError(request,response,"Starting Date Missing",TemplatePathManager.packageDetails);
            return;
        }


        String[]  optionalProducts = new String[0];
        if(request.getParameterValues("optionalProducts")!=null){
            optionalProducts = request.getParameterValues("optionalProducts");
        }

        Order order = null;

        try {
            //TRY CREATE THE CONTRACT
            order = this.contractServices.createContract(pkgId , Integer.parseInt(rateId), optionalProducts,startingDate);
        } catch (ElementNotFound e) {
            this.setError(request,response,"Order creation failed",TemplatePathManager.contract);
        }

            //FIND LOGGED USER
            HttpSession session = request.getSession();
            if(session.getAttribute("user")!=null){
                User u = (User) session.getAttribute("user");
                order.setUser(u);

                session.setAttribute("order",order);
                request.setAttribute("order",order);
                this.templateRenderer(request, response, TemplatePathManager.contract);
                return;
            }
            else {
                session.setAttribute("order",order);
                this.setError(request,response,"To continue the purchase you need to log in!", TemplatePathManager.loginPage);
            }
    }
}

