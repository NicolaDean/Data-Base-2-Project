package it.polimi.db2.controllers;

import it.polimi.db2.entitys.Order;
import it.polimi.db2.services.ContractServices;
import it.polimi.db2.utils.TemplatePathManager;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "solve-payment", value = "/solve-payment")

public class SolvePayment extends BasicServerlet {

    @EJB(name = "it.polimi.db2.services/ContractServices")
    private ContractServices contractServices;
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String input = request.getParameter("id");
        int id = 0;

        try
        {
            id = Integer.parseInt(input);
            Order order = this.contractServices.getOrderById(id);

            HttpSession session = request.getSession();
            session.setAttribute("order",order);
            request.setAttribute("order",order);
            this.templateRenderer(request, response, TemplatePathManager.contract);

        }catch(Exception e)
        {
            this.setError(request, response, "Order not found", TemplatePathManager.error);
        }



    }




}

