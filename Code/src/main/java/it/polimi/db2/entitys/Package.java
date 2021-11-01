package it.polimi.db2.entitys;

import javax.persistence.*;
import java.util.List;

@Entity
@NamedQuery(name="Packages.All",query="select p from Package p")
@Table(name = "Packages", schema = "test")
public class Package {
    @Id
    @GeneratedValue
    int     id;
    String  name;


    @OneToMany
    @JoinColumn(name = "packageId")
    List<Service> services;

    @OneToMany
    @JoinColumn(name = "packageId")
    List<RateCost> rates;

    @JoinTable(
            name = "Packages_OptionalProducts",
            schema = "test",
            joinColumns = @JoinColumn(name = "packageId"),
            inverseJoinColumns = @JoinColumn(name = "productId"))
    @ManyToMany
    List<OptionalProduct> products;
}
