package it.polimi.db2.controllers;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import it.polimi.db2.entitys.Order;

import it.polimi.db2.entitys.custom.ReportData;
import it.polimi.db2.services.ReportServices;
import it.polimi.db2.utils.TemplatePathManager;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "report", value = "/report")

public class SalesReport extends BasicServerlet{
    @EJB(name = "it.polimi.db2.services/ReportServices")
    private ReportServices reportServices;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            List<Object[]> data = (List<Object[]>)this.reportServices.getNumberOfPurchaseByID();
            List<ReportData> reportData = new LinkedList<>();
            for(int i = 0; i < data.size(); i++)
            {
                System.out.println("--");
                Object[] pack = data.get(i);
                reportData.add(new ReportData((String) pack[0],(Long) pack[1]));
                System.out.println("--");
            }


          //  checkLogIn(request);


            request.setAttribute("reports", reportData);
            request.setAttribute("totalPurchases", this.totalPurchases(reportData));
           // request.setAttribute("number_of_orders",reportServices.size());
        }catch (Exception e)
        {
            e.printStackTrace();
        }



        this.templateRenderer(request,response, TemplatePathManager.report);


    }
    public int totalPurchases(List<ReportData> reportData){
        int counter=0;
        for(int i = 0; i < reportData.size(); i++){
            counter= (int) (counter+reportData.get(i).getCount());
        }
        return counter;
    }
}
