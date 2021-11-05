package it.polimi.db2.controllers;

import it.polimi.db2.services.ContractServices;
import it.polimi.db2.utils.TemplatePathManager;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "payment", value = "/payment")
public class PaymentResult extends BasicServerlet {


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

}
