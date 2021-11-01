package it.polimi.db2.entitys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "OptionalProducts", schema = "test")
public class OptionalProduct{

    @Id
    @GeneratedValue
    int     id;
    String  name;
    int     monthlyFee;

    public String getName() {
        return name;
    }
}
