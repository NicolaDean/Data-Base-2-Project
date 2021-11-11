package it.polimi.db2.controllers;

import it.polimi.db2.entitys.Order;
import it.polimi.db2.entitys.User;
import it.polimi.db2.entitys.custom.*;
import it.polimi.db2.services.ReportServices;
import it.polimi.db2.utils.TemplatePathManager;

import javax.ejb.EJB;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "report", value = "/report")

public class SalesReport extends BasicServerlet{
    @EJB(name = "it.polimi.db2.services/ReportServices")
    private ReportServices reportServices;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            //Getting Data
            List<Integer> totalSales= this.reportServices.purchasesTotalSales();
            List<PurchasesCount> purchasesCounts= this.reportServices.purchasesCounts();
            List<PurchasesCountGrouped> purchasesCountsGrouped= this.reportServices.purchasesCountsGrouped();
            List<OptionalProductsAverage> optionalProductsAverages= this.reportServices.optionalProductsAverages();
            List<ValueOfSalesDetailed> valueOfSalesDetailed= this.reportServices.valueOfSalesDetailed();
            List<InsolventReport> insolventReports=this.reportServices.insolventReports();
            List<User> usersInsolvent= this.reportServices.usersInsolvent();
            List<Order> ordersSuspended= this.reportServices.ordersSuspended();
            List<OptionalProductBestSeller> optionalProductBestSellersForValue=this.reportServices.optionalProductBestSellersForValue();
            List<OptionalProductBestSeller> optionalProductBestSellersForAmount=this.reportServices.optionalProductBestSellersForAmount();


            //Setting Attributes
            if(purchasesCounts.size()>0) {
                request.setAttribute("insolventReports", insolventReports);
                request.setAttribute("usersInsolvent", usersInsolvent);
                request.setAttribute("ordersSuspended", ordersSuspended);
                request.setAttribute("valueOfSalesDetailed", valueOfSalesDetailed);
                request.setAttribute("purchasesCounts", purchasesCounts);
                request.setAttribute("purchasesCountsGrouped", purchasesCountsGrouped);
                request.setAttribute("totalPurchases", this.reportServices.purchasesTotalSum(purchasesCountsGrouped));
                request.setAttribute("totalSales", totalSales.get(0));
                request.setAttribute("optionalProductsAverages", optionalProductsAverages);
                if (optionalProductBestSellersForValue.size() > 0) {
                    request.setAttribute("optionalProductBestSellersForValue", optionalProductBestSellersForValue.get(0));
                    request.setAttribute("optionalProductBestSellersForAmount", optionalProductBestSellersForAmount.get(0));
                }
            }

            this.templateRenderer(request,response, TemplatePathManager.report);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public int totalPurchases(List<PurchasesCount> reportData){
        int counter=0;
        for(int i = 0; i < reportData.size(); i++){
            counter= (int) (counter+reportData.get(i).getCount());
        }
        return counter;
    }


}
