package it.polimi.db2.services;

import javax.ejb.Stateless;
import javax.persistence.Table;
import java.util.List;

@Stateless
public class ReportServices extends BasicService{

    public List<ReportServices> getTotalPurchases(){
        return this.em.createQuery(
                "select count (distinct o)" +
                        "from Order o" +
                        " group by o.pack", ReportServices.class).getResultList();
    }

    public int getTotalNumberOfPurchases(){
        return getTotalPurchases().size();
    }
}
