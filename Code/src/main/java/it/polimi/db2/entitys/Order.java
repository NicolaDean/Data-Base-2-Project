package it.polimi.db2.entitys;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Orders", schema = "test")
@NamedQuery(name="Orders.Id"             ,  query="select o from Order o where o.Id = :orderId")
@NamedQuery(name="Orders.All"             , query="select o from Order o") //TODO adjust query for only accepted orders
@NamedQuery(name="Orders.Suspended"       , query="select o from Order o where o.status=false")
@NamedQuery(name="PurchasesByPackages"    , query = "select count (distinct o) from Order o group by o.pack")
@NamedQuery(name="Orders.UserInsolvances" , query = "select o from Order o where o.status=false and o.user.id = :userId")
//TODO think a new entity for statistical pourpose should be created to store pair "package-> quantity"

public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int Id;
    Date startDate;
    Date creationDate;
    float totalPayment;
    boolean status;

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

    public RateCost getRate() {
        return rate;
    }

    public List<OptionalProduct> getProducts() {
        return products;
    }

    public Package getPackage() {
        return pack;
    }

    public float getTotalPayment() {
        return totalPayment;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public float getTotalMonthlyPayment() {
        return (totalPayment/rate.monthValidity);
    }

    public void setStatus(boolean b) {
        this.status = b;
    }

    public int getId() {
        return Id;
    }
}
