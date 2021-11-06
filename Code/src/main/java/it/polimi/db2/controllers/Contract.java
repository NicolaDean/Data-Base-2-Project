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

import static javax.swing.text.html.CSS.getAttribute;

@WebServlet(name = "contract", value = "/contract")
public class Contract extends BasicServerlet{

    @EJB(name="it.polimi.db2.services/ContractServices")
    private ContractServices contractServices;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Order order = new Order();
//TODO add Start date to html and contract creation
        String rateId;
        rateId = StringEscapeUtils.escapeJava(request.getParameter("rates"));
        String packageId ;
        packageId= StringEscapeUtils.escapeJava(request.getParameter("packId"));

        String [] optionalProducts = request.getParameterValues("optionalProducts");

        //FIND LOGGED USER
        HttpSession session = request.getSession();
        User u = (User)session.getAttribute("user");


        try {
            this.contractServices.createContract(u.getId(),Integer.parseInt(packageId),Integer.parseInt(rateId),optionalProducts);
        } catch (ElementNotFound e) {
            e.printStackTrace();
            //TODO ERROR PAGE
        }


        this.templateRenderer(request,response, TemplatePathManager.contract);


    }

}
