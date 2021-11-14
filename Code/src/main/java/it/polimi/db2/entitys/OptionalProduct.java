package it.polimi.db2.entitys;

import javax.persistence.*;

@Entity
@Table(name = "OptionalProducts", schema = "test")
@NamedQuery(name= "OptionalProd.findAll",query = "select o from OptionalProduct o")
public class OptionalProduct{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int     id;
    String  name;
    int     monthlyFee;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getMonthlyFee() {
        return monthlyFee;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMonthlyFee(int monthlyFee) {
        this.monthlyFee = monthlyFee;
    }
}
