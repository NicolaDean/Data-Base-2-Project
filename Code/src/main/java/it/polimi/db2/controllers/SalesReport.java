package it.polimi.db2.controllers;

import it.polimi.db2.entitys.Order;

import it.polimi.db2.entitys.custom.ReportData;
import it.polimi.db2.services.ReportServices;
import it.polimi.db2.utils.TemplatePathManager;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "report", value = "/report")

public class SalesReport extends BasicServerlet{
    @EJB(name = "it.polimi.db2.services/ReportServices")
    private ReportServices reportServices;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            List<ReportData> reportServices = this.reportServices.getNumberOfPurchaseByID();
            for(int i = 0; i < reportServices.size(); i++)
            {
                System.out.println("package : " + reportServices.get(i).getId() + "number of purchases:" + reportServices.get(i).getCount());
            }
            checkLogIn(request);


            request.setAttribute("orders", reportServices);
            request.setAttribute("number_of_orders",reportServices.size());
        }catch (Exception e)
        {
            e.printStackTrace();
        }



        this.templateRenderer(request,response, TemplatePathManager.report);


    }
}
