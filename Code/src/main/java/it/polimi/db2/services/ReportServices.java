package it.polimi.db2.services;

import it.polimi.db2.entitys.Order;
import it.polimi.db2.entitys.custom.ReportData;

import javax.ejb.Stateless;
import javax.persistence.Table;
import java.util.List;

@Stateless
public class ReportServices extends BasicService{


    public List<Integer>  getNumberOfPurchase(){
        return this.em.createNamedQuery("PurchasesByPackages",Integer.class).getResultList();
    }

    public List<ReportData> getNumberOfPurchaseByID(){
        return this.em.createNamedQuery("PurchasesByPackagesID",ReportData.class).getResultList();
    }
    /**
     *
     * @return all suspended Orders waiting for payment
     */
    public List<Order> findPendingOrder()
    {
       return this.em.createNamedQuery("Orders.Suspended",Order.class).getResultList();
    }

}
