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
}
