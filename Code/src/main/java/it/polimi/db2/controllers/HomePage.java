package it.polimi.db2.controllers;

import it.polimi.db2.entitys.Order;
import it.polimi.db2.entitys.Package;
import it.polimi.db2.entitys.User;
import it.polimi.db2.services.PackageService;
import it.polimi.db2.services.UserServices;
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

    @EJB(name="it.polimi.db2.services/UserService")
    private UserServices userServices;


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {


        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");


        if(user!=null && user.isInsolvent())
        {
            List<Order> orders = this.userServices.getUserSuspendedOrders(user);
            System.out.println("Order Size " + orders.size());
            //Set as atribute eventual insolvent orders
            request.setAttribute("insolventOrder",orders);
        }
        List<Package> packages = this.packageService.getAllPackages();
        request.setAttribute("packages",packages);

        this.templateRenderer(request,response, TemplatePathManager.homePage);
    }
}
