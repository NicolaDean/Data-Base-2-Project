package it.polimi.db2.entitys;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Orders", schema = "test")
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int Id;
    Date startDate;
    Date creationDate;
    float totalPayment;

    @OneToOne
    @JoinColumn(name = "userId")
    User user;

    @OneToOne
    @JoinColumn(name = "rateId")
    RateCost rate;

    @OneToOne
    @JoinColumn(name = "packageId")
    Package pack;

    @JoinTable(
            name = "Orders_OptionalProducts",
            schema = "test",
            joinColumns = @JoinColumn(name = "orderId"),
            inverseJoinColumns = @JoinColumn(name = "productId"))
    @ManyToMany
    List<OptionalProduct> products;

    public void addOptionalProduct(OptionalProduct op) {
        this.products.add(op);
    }

    public void setRate(RateCost rate) {

        this.rate = rate;
    }

    public void setPackage(Package p) {
        this.pack = p;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void calculateTotalSum()
    {
        int tot = 0;

        tot = rate.cost * rate.monthValidity;

        for(OptionalProduct p : products)
        {
            tot += p.getMonthlyFee()*rate.monthValidity;
        }

        this.totalPayment = tot;

        startDate = new Date();
        creationDate = new Date();
    }

    public void addOptionalProducts(List<OptionalProduct> products) {
        this.products = products;
    }
}
