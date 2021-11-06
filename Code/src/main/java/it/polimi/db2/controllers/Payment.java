package it.polimi.db2.controllers;

import it.polimi.db2.services.ContractServices;
import it.polimi.db2.utils.TemplatePathManager;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

@WebServlet(name = "payment", value = "/payment")
public class Payment extends BasicServerlet {

    private Random random= new Random();
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String paymentConfirmation;
          if(getRandomBoolean()){
              paymentConfirmation= "CONGRATULATIONS, PAYMENT ACCEPTED!";
          }
          else paymentConfirmation="PAYMENT DECLINED, PLEASE RETRY THE TRANSACTION";
        request.setAttribute("paymentConfirmation", paymentConfirmation);
        this.templateRenderer(request, response, TemplatePathManager.payment);

    }

    public boolean getRandomBoolean() {
        return random.nextBoolean();
    }
}
