package it.polimi.db2.services;

import it.polimi.db2.entitys.Order;
import it.polimi.db2.entitys.User;
import it.polimi.db2.entitys.custom.*;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Vector;

@Stateless
public class ReportServices extends BasicService{


    public List<Integer>  getNumberOfPurchase(){
        return this.em.createNamedQuery("PurchasesByPackages",Integer.class).getResultList();
    }

    public Vector<Object[]> getNumberOfPurchaseByID(){
        return (Vector)this.em.createNamedQuery("PurchasesByPackagesID",Object.class).getResultList();
    }
    /**
     *
     * @return all suspended Orders waiting for payment
     */
    public List<Order> findPendingOrder()
    {
       return this.em.createNamedQuery("Orders.Suspended",Order.class).getResultList();
    }

    public List<OptionalProductsAverage> optionalProductsAverages(){
      return   this.em.createNamedQuery("Report.OptionalProductAverage",OptionalProductsAverage.class).getResultList();
    }

    public List<PurchasesCount> purchasesCounts(){
        return this.em.createNamedQuery("Report.PurchasesCount",PurchasesCount.class).getResultList();
    }

    public List<PurchasesCountGrouped> purchasesCountsGrouped(){
        return this.em.createNamedQuery("Report.PurchasesCountGrouped",PurchasesCountGrouped.class).getResultList();
    }

    public List<ValueOfSalesDetailed> valueOfSalesDetailed(){
        return this.em.createNamedQuery("Report.SalesDetailed",ValueOfSalesDetailed.class).getResultList();
    }

    public List<Order> ordersSuspended(){
        return this.em.createNamedQuery("Orders.Suspended",Order.class).getResultList();
    }

    public List<User> usersInsolvent(){
        return this.em.createNamedQuery("User.insolvent", User.class).getResultList();
    }

    public List<InsolventReport> insolventReports(){
        return this.em.createNamedQuery("Report.InsolventReport", InsolventReport.class).getResultList();
    }

    public int purchasesTotalSum(List<PurchasesCountGrouped> purchasesCounts){
        int counter=0;
        for(int i=0;i<purchasesCounts.size();i++){
           counter= (int) (counter + purchasesCounts.get(i).getCount());
        }
        return counter;
    }

    public List<OptionalProductBestSeller> optionalProductBestSellers(){
        return this.em.createNamedQuery("Report.OptionalProductBestSeller",OptionalProductBestSeller.class).getResultList();
    }
    public List<OptionalProductBestSeller> optionalProductBestSellersForValue(){
        return this.em.createNamedQuery("Report.OptionalProductBestSellerForValue",OptionalProductBestSeller.class).getResultList();
    }
    public List<OptionalProductBestSeller> optionalProductBestSellersForAmount(){
        return this.em.createNamedQuery("Report.OptionalProductBestSellerForAmount",OptionalProductBestSeller.class).getResultList();
    }
    public List<Integer> purchasesTotalSales(){
        return this.em.createNamedQuery("Report.SalesTotal",Integer.class).getResultList();
    }
}
