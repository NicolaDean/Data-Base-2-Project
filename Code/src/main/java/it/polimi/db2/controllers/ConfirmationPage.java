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

@WebServlet(name = "confirmation", value = "/confirmation")
public class ConfirmationPage extends BasicServerlet {


    @EJB(name = "it.polimi.db2.services/ContractServices")
    private ContractServices contractServices;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String rateId;
        rateId = StringEscapeUtils.escapeJava(request.getParameter("rates"));
        String packageId;
        packageId = StringEscapeUtils.escapeJava(request.getParameter("packId"));

        String[]  optionalProducts = new String[0];
        if(request.getParameterValues("optionalProducts")!=null){
            optionalProducts = request.getParameterValues("optionalProducts");
        }

        Order order = null;

        try {
            //TRY CREATE THE CONTRACT
            order = this.contractServices.createContract( Integer.parseInt(packageId), Integer.parseInt(rateId), optionalProducts);
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

