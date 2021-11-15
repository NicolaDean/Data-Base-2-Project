package it.polimi.db2.entitys;

import javax.persistence.*;

@Entity
@Table(name="Rate_costs", schema = "test")
public class RateCost {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int id;
    int monthValidity;
    int cost;
    int packageId;

    public int getPrice() {
        return cost;
    }

    public int getMonthValidity() {
        return monthValidity;
    }

    public int getId() {
        return id;
    }

    public int getPackageId() {
        return packageId;
    }

    public String getRatesString()
    {
        return this.cost + "$ for " + this.monthValidity + " month";
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setMonthValidity(int monthValidity) {
        this.monthValidity = monthValidity;
    }
}
