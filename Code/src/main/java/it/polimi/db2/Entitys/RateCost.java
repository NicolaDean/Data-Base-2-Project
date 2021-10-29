package it.polimi.db2.Entitys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Rate_costs", schema = "test")
public class RateCost {

    @Id
    @GeneratedValue
    int id;
    int monthValidity;
    int cost;
    int packageId;
}
