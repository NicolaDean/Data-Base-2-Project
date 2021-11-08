package it.polimi.db2.services;

import it.polimi.db2.entitys.Order;
import it.polimi.db2.entitys.custom.OptionalProductsAverage;

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

}
