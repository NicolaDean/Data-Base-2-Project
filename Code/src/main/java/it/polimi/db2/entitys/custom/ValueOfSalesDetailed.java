package it.polimi.db2.entitys.custom;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@NamedQuery(name = "Report.SalesDetailed", query = "select v from ValueOfSalesDetailed v")
@Table(name="ValueOfSalesDetailed", schema="test")
public class ValueOfSalesDetailed {
    @Id
    private String name;
    private int totalPayment;
    private int totalPaymentWithoutOP;

    public String getName() {
        return name;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public int getTotalPaymentWithoutOP() {
        return totalPaymentWithoutOP;
    }
}
