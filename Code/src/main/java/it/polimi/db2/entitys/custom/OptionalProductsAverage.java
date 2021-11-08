package it.polimi.db2.entitys.custom;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "Report.OptionalProductAverage", query = "select op from OptionalProductsAverage op")
@Table(name="OptionalProductsAverage", schema="test")
public class OptionalProductsAverage {
    @Id
    String name;
    Float avg;

    public Float getAvg() {
        return avg;
    }

    public String getName() {
        return name;
    }

}
