package it.polimi.db2.entitys.custom;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
//All packages with counts per rate
@NamedQuery(name = "Report.PurchasesCount", query = "select p from PurchasesCount p")
@Table(name="PurchasesCount", schema="test")
public class PurchasesCount {
@Id
    private String name;
    private String validity;
    private int count;

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }

    public String getValidity() {
        return validity;
    }
}
