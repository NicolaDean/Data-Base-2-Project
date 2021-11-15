package it.polimi.db2.controllers;

import it.polimi.db2.entitys.Order;
import it.polimi.db2.entitys.User;
import it.polimi.db2.exception.ElementNotFound;
import it.polimi.db2.services.ContractServices;
import it.polimi.db2.services.UserServices;
import it.polimi.db2.utils.TemplatePathManager;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Random;

@WebServlet(name = "payment", value = "/payment")
public class Payment extends BasicServerlet {


    @EJB(name = "it.polimi.db2.services/ContractServices")
    private ContractServices contractServices;
    @EJB(name = "it.polimi.db2.services/User")
    private UserServices userServices;


    private Random random= new Random();
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //TODO WHEN CREATE FILTER PUT A "divieto" ON PAYMENT WITH EMPTY SESSION
        HttpSession session = request.getSession();

        Order order = (Order) session.getAttribute("order");
        User  user  = (User) session.getAttribute("user") ;
        boolean flag = false;
        if(order.toUpdate())
        {
            //If user tries to solve a payment then payment will be successful (flag=true), else will be random
            flag = true;
        }
        String paymentConfirmation;

          if(getRandomBoolean(flag)){

              paymentConfirmation= "CONGRATULATIONS, PAYMENT ACCEPTED!";

              order.setStatus(true);
          }
          else{
              order.setStatus(false);
              paymentConfirmation="PAYMENT DECLINED, PLEASE RETRY THE TRANSACTION";
              user.setInsolvent(true);
          }

        try {
            if(flag) this.contractServices.removeSuspendFromOrder(order.getId());
            else this.contractServices.persist(order);

        } catch (ElementNotFound e) {
            e.printStackTrace();
        }

        request.setAttribute("paymentConfirmation", paymentConfirmation);
        this.templateRenderer(request, response, TemplatePathManager.payment);

    }

    public boolean getRandomBoolean(boolean flag) {
        if (flag==false){
            return random.nextBoolean();
        }
        else return true;
    }
}
