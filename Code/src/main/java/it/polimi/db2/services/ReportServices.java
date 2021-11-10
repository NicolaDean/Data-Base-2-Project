package it.polimi.db2.services;

import it.polimi.db2.entitys.Order;
import it.polimi.db2.entitys.User;
import it.polimi.db2.entitys.custom.OptionalProductsAverage;
import it.polimi.db2.entitys.custom.PurchasesCount;
import it.polimi.db2.entitys.custom.PurchasesCountGrouped;
import it.polimi.db2.entitys.custom.ValueOfSalesDetailed;

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

    //TODO
    public List<Order> ordersSuspended(){
        return this.em.createNamedQuery("Orders.Suspended",Order.class).getResultList();
    }

    public List<User> usersInsolvent(){
        return this.em.createNamedQuery("User.insolvent", User.class).getResultList();
    }


    public int purchasesTotalSum(List<PurchasesCountGrouped> purchasesCounts){
        int counter=0;
        for(int i=0;i<purchasesCounts.size();i++){
           counter= (int) (counter + purchasesCounts.get(i).getCount());
        }
        return counter;
    }

}
