package it.polimi.db2.entitys;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Orders", schema = "test")
public class Order {

    @Id
    int Id;
    Date startDate;
    Date creationDate;
    float totalPayment;

    @OneToOne
    @JoinColumn(name = "userId")
    User user;

    @OneToOne
    @JoinColumn(name = "rateId")
    RateCost rates;

    @OneToOne
    @JoinColumn(name = "packageId")
    Package p;

    @JoinTable(
            name = "Orders_OptionalProducts",
            schema = "test",
            joinColumns = @JoinColumn(name = "orderId"),
            inverseJoinColumns = @JoinColumn(name = "productId"))
    @ManyToMany
    List<OptionalProduct> products;
}
