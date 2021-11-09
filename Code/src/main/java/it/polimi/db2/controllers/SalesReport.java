package it.polimi.db2.controllers;

import it.polimi.db2.entitys.custom.OptionalProductsAverage;
import it.polimi.db2.entitys.custom.PurchasesCount;
import it.polimi.db2.entitys.custom.PurchasesCountGrouped;
import it.polimi.db2.services.ReportServices;
import it.polimi.db2.utils.TemplatePathManager;

import javax.ejb.EJB;
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
            List<PurchasesCount> purchasesCounts= this.reportServices.purchasesCounts();
            List<PurchasesCountGrouped> purchasesCountsGrouped= this.reportServices.purchasesCountsGrouped();
            request.setAttribute("purchasesCounts",purchasesCounts);
            request.setAttribute("purchasesCountsGrouped",purchasesCountsGrouped);
            request.setAttribute("totalPurchases",this.reportServices.purchasesTotalSum(purchasesCountsGrouped));
            List<OptionalProductsAverage> optionalProductsAverages= this.reportServices.optionalProductsAverages();
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
