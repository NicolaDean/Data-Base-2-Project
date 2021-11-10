package it.polimi.db2.entitys.custom;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "Report.OptionalProductBestSeller", query = "select o from OptionalProductBestSeller o")
@NamedQuery(name = "Report.OptionalProductBestSellerForValue", query = "select o from OptionalProductBestSeller o where o.value=(select max(o2.value) from OptionalProductBestSeller o2)")
@NamedQuery(name = "Report.OptionalProductBestSellerForAmount", query = "select o from OptionalProductBestSeller o where o.amountSold=(select max(o2.amountSold) from OptionalProductBestSeller o2)")
@Table(name="OptionalProductBestSeller", schema="test")
public class OptionalProductBestSeller {
    @Id
    private String name;
    private int amountSold;
    private int value;

    public int getAmountSold() {
        return amountSold;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
