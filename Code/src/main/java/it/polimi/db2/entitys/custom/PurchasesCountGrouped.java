package it.polimi.db2.entitys.custom;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
//All packages with counts per package
@NamedQuery(name = "Report.PurchasesCountGrouped", query = "select p from PurchasesCountGrouped p")
@Table(name="PurchasesCountGrouped", schema="test")
public class PurchasesCountGrouped {
    @Id
    private String name;
    private int count;

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}
